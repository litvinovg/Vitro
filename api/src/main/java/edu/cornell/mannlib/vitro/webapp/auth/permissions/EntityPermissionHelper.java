package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdf.model.impl.Util;
import org.apache.jena.shared.Lock;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display.DisplayDataProperty;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display.DisplayDataPropertyStatement;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display.DisplayObjectProperty;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display.DisplayObjectPropertyStatement;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display.ObjectPropertyAccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt.DataPropertyStatementAccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt.ObjectPropertyStatementAccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt.PropertyStatementAccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.publish.PublishDataProperty;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.publish.PublishDataPropertyStatement;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.publish.PublishObjectProperty;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.publish.PublishObjectPropertyStatement;
import edu.cornell.mannlib.vitro.webapp.beans.DataPropertyStatement;
import edu.cornell.mannlib.vitro.webapp.beans.FauxProperty;
import edu.cornell.mannlib.vitro.webapp.beans.ObjectProperty;
import edu.cornell.mannlib.vitro.webapp.beans.Property;
import edu.cornell.mannlib.vitro.webapp.dao.PropertyDao;
import edu.cornell.mannlib.vitro.webapp.dao.PropertyDao.FullPropertyKey;
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelAccess;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelNames;
import edu.cornell.mannlib.vitro.webapp.utils.RelationshipChecker;
import edu.cornell.mannlib.vitro.webapp.web.templatemodels.individual.FauxPropertyWrapper;

public class EntityPermissionHelper {

    static final Log log = LogFactory.getLog(EntityPermissionHelper.class);

    
    static boolean isAuthorizedForByEntityPermission(PropertyStatementAccessObject action, List<String> personUris, EntityPermission entityPermission) {
        // If we are not limiting to only objects that the user has a relationship with
        // We can just authorise the access right now
        if (!entityPermission.isLimitToRelatedUser()) {
            return true;
        }
        
        // Nothing to authorise if no person list is supplied
        if (personUris == null) {
            return false;
        }
        
        // Obtain the subject and object URIs
        String subjectUri = null;
        String objectUri = null;
        
        if (action instanceof DataPropertyStatementAccessObject) {
            subjectUri = ((DataPropertyStatementAccessObject)action).getSubjectUri();
        } else if (action instanceof ObjectPropertyStatementAccessObject) {
            subjectUri = ((ObjectPropertyStatementAccessObject)action).getSubjectUri();
            objectUri = ((ObjectPropertyStatementAccessObject)action).getObjectUri();
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

    static boolean isAuthorizedForByEntityPermission(Property prop, EntityPermission entityPermission) {
        if (AccessObject.SOME_URI.equals(prop.getURI())) {
            return true;
        }
        if (entityPermission.getAuthorizedKeys().contains(new PropertyDao.FullPropertyKey(prop))) {
            return true;
        }
        if (prop instanceof FauxPropertyWrapper) {
            return entityPermission.getAuthorizedKeys().contains(new PropertyDao.FullPropertyKey(((FauxPropertyWrapper) prop).getConfigUri()));
        }
        return entityPermission.getAuthorizedKeys().contains(new PropertyDao.FullPropertyKey(prop.getURI()));
    }

   
    static boolean isModifiable(String uri) {
        if (EntityPermissionHelper.PROHIBITED_NAMESPACES.contains(uri.substring(0, Util.splitNamespaceXML(uri)))) {
            if (EntityPermissionHelper.PERMITTED_EXCEPTIONS.contains(uri)) {
                return true;
            } else {
                return false;
            }
        }
    
        return true;
    
    }

    static final Collection<String> PROHIBITED_NAMESPACES = Arrays
    .asList(VitroVocabulary.vitroURI, "" );
    static final Collection<String> PERMITTED_EXCEPTIONS = Arrays
    .asList(VitroVocabulary.MONIKER,
            VitroVocabulary.MODTIME, VitroVocabulary.IND_MAIN_IMAGE,
            VitroVocabulary.LINK, VitroVocabulary.PRIMARY_LINK,
            VitroVocabulary.ADDITIONAL_LINK,
            VitroVocabulary.LINK_ANCHOR, VitroVocabulary.LINK_URL );


    

    static void updateForEntityPermission(Property p, EntityPermission entityPermission) {
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
            if (accountsModel.contains(accountsModel.getResource(entityPermission.getUri()), accountsModel.getProperty(VitroVocabulary.PERMISSION_FOR_ENTITY), accountsModel.getResource(uri))) {
                entityPermission.getAuthorizedKeys().add(key);
                entityPermission.getAuthorizedResources().add(uri);
            } else {
                entityPermission.getAuthorizedKeys().remove(key);
                entityPermission.getAuthorizedResources().remove(uri);
            }
        } finally {
            accountsModel.leaveCriticalSection();
        }
    }

    static void updateEntityPermission(Map<String, PropertyDao.FullPropertyKey> propertyKeyMap, EntityPermission entityPermission) {
        List<PropertyDao.FullPropertyKey> newKeys = new ArrayList<>();
        List<String> newResources = new ArrayList<>();
    
        OntModel accountsModel = ModelAccess.getInstance().getOntModel(ModelNames.USER_ACCOUNTS);
        accountsModel.enterCriticalSection(Lock.READ);
        StmtIterator propIter = null;
        try {
            propIter = accountsModel.listStatements(
                accountsModel.getResource(entityPermission.getUri()),
                accountsModel.getProperty(VitroVocabulary.PERMISSION_FOR_ENTITY),
                (RDFNode) null
            );
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
        entityPermission.getAuthorizedKeys().clear();
        entityPermission.getAuthorizedKeys().addAll(newKeys);
        
        // replace authorized resources
        entityPermission.getAuthorizedResources().clear();
        entityPermission.getAuthorizedResources().addAll(newResources);
    }

    static boolean isAuthorizedBySimplePermission(AccessObject whatToAuth, SimplePermission simplePermission) {
        if (whatToAuth != null) {
    		if (simplePermission.getUri().equals(whatToAuth.getURI())) {
    			SimplePermission.log.debug(simplePermission + " authorizes " + whatToAuth);
    			return true;
    		}
    	}
    	SimplePermission.log.debug(simplePermission + " does not authorize " + whatToAuth);
    	return false;
    }
    
    static boolean isAuthorizedByEntityPublishPermission(List<String> personUris, AccessObject whatToAuth, EntityPermission entityPublishPermission) {
        boolean result = false;
    
        if (whatToAuth instanceof PublishDataProperty) {
            String predicateUri = ((PublishDataProperty)whatToAuth).getDataProperty().getURI();
            result = isAuthorizedForByEntityPermission(new Property(predicateUri), entityPublishPermission);
        } else if (whatToAuth instanceof PublishObjectProperty) {
            ObjectProperty op = ((PublishObjectProperty)whatToAuth).getObjectProperty();
            result = isAuthorizedForByEntityPermission(op, entityPublishPermission);
        } else if (whatToAuth instanceof PublishDataPropertyStatement) {
    
            // Subject [((PublishDataPropertyStatement)whatToAuth).getSubjectUri()] is a resource
            // Previous auth code always evaluated as true when checking permissions for resources
            // Do we need to implement a check on permissions the class for the resource?
    
            String predicateUri = ((PublishDataPropertyStatement)whatToAuth).getPredicateUri();
            result = isAuthorizedForByEntityPermission(new Property(predicateUri), entityPublishPermission);
        } else if (whatToAuth instanceof PublishObjectPropertyStatement) {
    
            // Subject [((PublishObjectPropertyStatement)whatToAuth).getSubjectUri()] is a resource
            // Object  [((PublishObjectPropertyStatement)whatToAuth).getObjectUri()] is a resource
            // Previous auth code always evaluated as true when checking permissions for resources
            // Do we need to implement a check on permissions the class for the resource?
    
            Property predicate = ((PublishObjectPropertyStatement)whatToAuth).getPredicate();
            result = isAuthorizedForByEntityPermission(predicate, entityPublishPermission);
        }
    
        if (result) {
            log.debug(entityPublishPermission + " authorizes " + whatToAuth);
        } else {
            log.debug(entityPublishPermission + " does not authorize " + whatToAuth);
        }
    
        return result;
    }

    static boolean isAuthorizedByEntityDisplayPermission(AccessObject whatToAuth, EntityDisplayPermission entityDisplayPermission) {
        boolean result = false;
    
        if (whatToAuth instanceof DisplayDataProperty) {
            String predicateUri = ((DisplayDataProperty)whatToAuth).getDataProperty().getURI();
            result = isAuthorizedForByEntityPermission(new Property(predicateUri), entityDisplayPermission);
        } else if (whatToAuth instanceof DisplayObjectProperty) {
            result = isAuthorizedForByEntityPermission(((ObjectPropertyAccessObject)whatToAuth).getObjectProperty(), entityDisplayPermission);
        } else if (whatToAuth instanceof DisplayDataPropertyStatement) {
            DataPropertyStatement stmt = ((DisplayDataPropertyStatement)whatToAuth).getDataPropertyStatement();
    
            // Subject [stmt.getIndividualURI()] is a resource
            // Previous auth code always evaluated as true when checking permissions for resources
            // Do we need to implement a check on permissions the class for the resource?
    
            String predicateUri = stmt.getDatapropURI();
            result = isAuthorizedForByEntityPermission(new Property(predicateUri), entityDisplayPermission);
        } else if (whatToAuth instanceof DisplayObjectPropertyStatement) {
    
            // Subject [((DisplayObjectPropertyStatement)whatToAuth).getSubjectUri()] is a resource
            // Object [((DisplayObjectPropertyStatement)whatToAuth).getObjectUri()] is resource
            // Previous auth code always evaluated as true when checking permissions for resources
            // Do we need to implement a check on permissions the class for the resource?
    
            Property op = ((DisplayObjectPropertyStatement)whatToAuth).getProperty();
            result = isAuthorizedForByEntityPermission(op, entityDisplayPermission);
        }
    
        if (result) {
            log.debug(entityDisplayPermission + " authorizes " + whatToAuth);
        } else {
            log.debug(entityDisplayPermission + " does not authorize " + whatToAuth);
        }
    
        return result;
    }
    
    static boolean isAuthorizedByEntityUpdatePermission(List<String> personUris, AccessObject whatToAuth, EntityUpdatePermission entityUpdatePermission) {
        boolean isAuthorized = false;
    
        if (whatToAuth instanceof DataPropertyStatementAccessObject) {
            // Check resource
            String subjectUri = ((DataPropertyStatementAccessObject)whatToAuth).getSubjectUri();
            if (isModifiable(subjectUri)) {
                Property predicate = ((DataPropertyStatementAccessObject)whatToAuth).getPredicate();
                if (isModifiable(predicate.getURI())) {
                    isAuthorized = isAuthorizedForByEntityPermission(predicate, entityUpdatePermission);
                }
            }
    
            if (isAuthorized) {
                isAuthorized = isAuthorizedForByEntityPermission((PropertyStatementAccessObject) whatToAuth, personUris, entityUpdatePermission);
            }
        } else if (whatToAuth instanceof ObjectPropertyStatementAccessObject) {
            String subjectUri = ((ObjectPropertyStatementAccessObject)whatToAuth).getSubjectUri();
            String objectUri = ((ObjectPropertyStatementAccessObject)whatToAuth).getObjectUri();
            if (isModifiable(subjectUri) && isModifiable(objectUri)) {
                Property predicate = ((ObjectPropertyStatementAccessObject)whatToAuth).getPredicate();
                if (isModifiable(predicate.getURI())) {
                    isAuthorized = isAuthorizedForByEntityPermission(predicate, entityUpdatePermission);
                }
            }
    
            if (isAuthorized) {
                isAuthorized = isAuthorizedForByEntityPermission((PropertyStatementAccessObject) whatToAuth, personUris, entityUpdatePermission);
            }
        } 
    
        if (isAuthorized) {
            log.debug(entityUpdatePermission + " authorizes " + whatToAuth);
        } else {
            log.debug(entityUpdatePermission + " does not authorize " + whatToAuth);
        }
    
        return isAuthorized;
    }

    static boolean isAuthorizedByBrokenPermission() {
        return false;
    }


}
