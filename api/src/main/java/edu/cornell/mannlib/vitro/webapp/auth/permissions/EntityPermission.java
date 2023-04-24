/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.ActionRequest;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt.AbstractDataPropertyStatementAction;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt.AbstractObjectPropertyStatementAction;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt.AbstractPropertyStatementAction;
import edu.cornell.mannlib.vitro.webapp.beans.FauxProperty;
import edu.cornell.mannlib.vitro.webapp.beans.Property;
import edu.cornell.mannlib.vitro.webapp.dao.PropertyDao;
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelAccess;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelNames;
import edu.cornell.mannlib.vitro.webapp.utils.RelationshipChecker;
import edu.cornell.mannlib.vitro.webapp.web.templatemodels.individual.FauxPropertyWrapper;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.shared.Lock;

import java.util.*;

/**
 * A permission that is to be applied to "entities"
 * An entity may be a class, property or faux property defined in the ontologies.
 * Subclass to define the type of permission that is being granted (e.g. display, update, publish)
 */
public abstract class EntityPermission extends Permission {

    protected EntityPermission(String uri) {
        super(uri);
    }

    /**
     * Instance fields for each EntityPermission
     */
    private final Set<PropertyDao.FullPropertyKey> authorizedKeys = new HashSet<>();
    private final Set<String> authorizedResources = new HashSet<>();
    boolean limitToRelatedUser = false;

    void update(Map<String, PropertyDao.FullPropertyKey> propertyKeyMap) {
        List<PropertyDao.FullPropertyKey> newKeys = new ArrayList<>();
        List<String> newResources = new ArrayList<>();

        OntModel accountsModel = ModelAccess.getInstance().getOntModel(ModelNames.USER_ACCOUNTS);
        accountsModel.enterCriticalSection(Lock.READ);
        StmtIterator propIter = null;
        try {
            propIter = getStatementsWithThisUri(accountsModel);
            while (propIter.hasNext()) {
                Statement proptStmt = propIter.next();
                if (proptStmt.getObject().isURIResource()) {
                    String uri = proptStmt.getObject().asResource().getURI();
                    PropertyDao.FullPropertyKey key = propertyKeyMap.get(uri);
                    if (key != null) {
                        newKeys.add(key);
                    } else {
                        newResources.add(uri);
                    }
                }
            }
        } finally {
            if (propIter != null) {
                propIter.close();
            }
            accountsModel.leaveCriticalSection();
        }

        // replace authorized keys
        synchronized (authorizedKeys) {
            authorizedKeys.clear();
            authorizedKeys.addAll(newKeys);
        }

        // replace authorized resources
        synchronized (authorizedResources) {
            authorizedResources.clear();
            authorizedResources.addAll(newResources);
        }
    }

    private StmtIterator getStatementsWithThisUri(OntModel accountsModel) {
        return accountsModel.listStatements(
                        accountsModel.getResource(this.uri),
                        accountsModel.getProperty(VitroVocabulary.PERMISSION_FOR_ENTITY),
                        (RDFNode) null
                    );
    }

    void updateFor(Property p) {
        String uri = null;      // Due to the data model of Vitro, this could be a property or a property config uri
        PropertyDao.FullPropertyKey key = null;

        if (p instanceof FauxProperty) {
            FauxProperty fp = (FauxProperty)p;
            uri = fp.getConfigUri();
        } else {
            uri = p.getURI();
        }
        key = new PropertyDao.FullPropertyKey(uri);

        OntModel accountsModel = ModelAccess.getInstance().getOntModel(ModelNames.USER_ACCOUNTS);
        accountsModel.enterCriticalSection(Lock.READ);

        try {
            if (accountsModel.contains(accountsModel.getResource(this.uri), accountsModel.getProperty(VitroVocabulary.PERMISSION_FOR_ENTITY), accountsModel.getResource(uri))) {
                synchronized (authorizedKeys) {
                    authorizedKeys.add(key);
                }

                synchronized (authorizedResources) {
                    authorizedResources.add(uri);
                }
            } else {
                synchronized (authorizedKeys) {
                    authorizedKeys.remove(key);
                }

                synchronized (authorizedResources) {
                    authorizedResources.remove(uri);
                }
            }
        } finally {
            accountsModel.leaveCriticalSection();
        }
    }

    protected boolean isAuthorizedFor(AbstractPropertyStatementAction action, List<String> personUris) {
        // If we are not limiting to only objects that the user has a relationship with
        // We can just authorise the access right now
        if (!limitToRelatedUser) {
            return true;
        }

        // Nothing to authorise if no person list is supplied
        if (personUris == null) {
            return false;
        }

        // Obtain the subject and object URIs
        String subjectUri = null;
        String objectUri = null;

        if (action instanceof AbstractDataPropertyStatementAction) {
            subjectUri = ((AbstractDataPropertyStatementAction)action).getSubjectUri();
        } else if (action instanceof AbstractObjectPropertyStatementAction) {
            subjectUri = ((AbstractObjectPropertyStatementAction)action).getSubjectUri();
            objectUri = ((AbstractObjectPropertyStatementAction)action).getObjectUri();
        }

        // If the subject or object is a user URI for the current user, authorise access
        for (String userUri : personUris) {
            if (subjectUri != null && subjectUri.equals(userUri)) {
                return true;
            }

            if (objectUri != null && objectUri.equals(userUri)) {
                return true;
            }
        }

        return RelationshipChecker.anyRelated(action.getOntModel(), Arrays.asList(action.getResourceUris()), personUris);
    }

    protected boolean isAuthorizedFor(Property prop) {
        if (ActionRequest.SOME_URI.equals(prop.getURI())) {
            return true;
        }
        synchronized (authorizedKeys) {
            if (authorizedKeys.contains(new PropertyDao.FullPropertyKey(prop))) {
                return true;
            }
	    	if (prop instanceof FauxPropertyWrapper) {
	    		return authorizedKeys.contains(new PropertyDao.FullPropertyKey(((FauxPropertyWrapper)prop).getConfigUri()));
	    	}
            return authorizedKeys.contains(new PropertyDao.FullPropertyKey(prop.getURI()));
        }
    }
}
