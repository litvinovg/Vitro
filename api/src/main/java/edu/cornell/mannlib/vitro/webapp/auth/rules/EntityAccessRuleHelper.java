package edu.cornell.mannlib.vitro.webapp.auth.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.rdf.model.impl.Util;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessObjectType;
import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessOperation;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.common.HasAssociatedIndividual;
import edu.cornell.mannlib.vitro.webapp.auth.objects.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;
import edu.cornell.mannlib.vitro.webapp.beans.ObjectProperty;
import edu.cornell.mannlib.vitro.webapp.beans.Property;
import edu.cornell.mannlib.vitro.webapp.dao.PropertyDao;
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary;
import edu.cornell.mannlib.vitro.webapp.utils.RelationshipChecker;
import edu.cornell.mannlib.vitro.webapp.web.templatemodels.individual.FauxPropertyWrapper;

public class EntityAccessRuleHelper {

    static final Log log = LogFactory.getLog(EntityAccessRuleHelper.class);

    
    static boolean isAuthorizedForByEntityPermission(AccessObject ao, List<String> personUris, EntityAccessRule entityPermission) {
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
        final AccessObjectType type = ao.getType();

        if (AccessObjectType.DATA_PROPERTY_STMT.equals(type)) {
            subjectUri = ao.getStatementSubject();
        } else if (AccessObjectType.OBJECT_PROPERTY_STMT.equals(type)) {
            subjectUri = ao.getStatementSubject();
            objectUri = ao.getStatementObject();
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
        
        return RelationshipChecker.anyRelated(ao.getStatementOntModel(), Arrays.asList(ao.getResourceUris()), personUris);
    }

    static boolean isAuthorizedForByEntityPermission(Property prop, EntityAccessRule entityPermission) {
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
        if (EntityAccessRuleHelper.PROHIBITED_NAMESPACES.contains(uri.substring(0, Util.splitNamespaceXML(uri)))) {
            return EntityAccessRuleHelper.PERMITTED_EXCEPTIONS.contains(uri);
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

    static boolean isAuthorizedByEntityPublishPermission(AccessObject whatToAuth, EntityAccessRule entityPublishPermission, AccessOperation operation) {
        boolean result = false;
        final AccessObjectType type = whatToAuth.getType();
        if (AccessObjectType.DATA_PROPERTY.equals(type)) {
            String predicateUri = whatToAuth.getDataProperty().getURI();
            result = isAuthorizedForByEntityPermission(new Property(predicateUri), entityPublishPermission);
        } else if (AccessObjectType.OBJECT_PROPERTY.equals(type)) {
            ObjectProperty op = whatToAuth.getObjectProperty();
            result = isAuthorizedForByEntityPermission(op, entityPublishPermission);
        } else if (AccessObjectType.DATA_PROPERTY_STMT.equals(type)) {
    
            // Subject [((PublishDataPropertyStatement)whatToAuth).getSubjectUri()] is a resource
            // Previous auth code always evaluated as true when checking permissions for resources
            // Do we need to implement a check on permissions the class for the resource?
    
            String predicateUri = whatToAuth.getPredicateUri();
            result = isAuthorizedForByEntityPermission(new Property(predicateUri), entityPublishPermission);
        } else if (AccessObjectType.OBJECT_PROPERTY_STMT.equals(type)) {
    
            // Subject [((PublishObjectPropertyStatement)whatToAuth).getSubjectUri()] is a resource
            // Object  [((PublishObjectPropertyStatement)whatToAuth).getObjectUri()] is a resource
            // Previous auth code always evaluated as true when checking permissions for resources
            // Do we need to implement a check on permissions the class for the resource?
    
            Property predicate = whatToAuth.getPredicate();
            result = isAuthorizedForByEntityPermission(predicate, entityPublishPermission);
        }
    
        if (result) {
            log.debug(entityPublishPermission + " authorizes " + whatToAuth);
        } else {
            log.debug(entityPublishPermission + " does not authorize " + whatToAuth);
        }
    
        return result;
    }

    static boolean isAuthorizedByEntityDisplayPermission(AccessObject whatToAuth, EntityAccessRule entityDisplayPermission, AccessOperation operation) {
        boolean result = false;
        final AccessObjectType type = whatToAuth.getType();
        
        if (AccessObjectType.DATA_PROPERTY.equals(type)) {
            String predicateUri = whatToAuth.getDataProperty().getURI();
            result = isAuthorizedForByEntityPermission(new Property(predicateUri), entityDisplayPermission);
        } else if (AccessObjectType.OBJECT_PROPERTY.equals(type)) {
            result = isAuthorizedForByEntityPermission(whatToAuth.getObjectProperty(), entityDisplayPermission);
        } else if (AccessObjectType.DATA_PROPERTY_STMT.equals(type)) {
            String predicateUri = whatToAuth.getPredicate().getURI();
    
            // Subject [stmt.getIndividualURI()] is a resource
            // Previous auth code always evaluated as true when checking permissions for resources
            // Do we need to implement a check on permissions the class for the resource?
    
            result = isAuthorizedForByEntityPermission(new Property(predicateUri), entityDisplayPermission);
        } else if (AccessObjectType.OBJECT_PROPERTY_STMT.equals(type)) {
    
            // Subject [((DisplayObjectPropertyStatement)whatToAuth).getSubjectUri()] is a resource
            // Object [((DisplayObjectPropertyStatement)whatToAuth).getObjectUri()] is resource
            // Previous auth code always evaluated as true when checking permissions for resources
            // Do we need to implement a check on permissions the class for the resource?
    
            Property op = whatToAuth.getPredicate();
            result = isAuthorizedForByEntityPermission(op, entityDisplayPermission);
        }
    
        if (result) {
            log.debug(entityDisplayPermission + " authorizes " + whatToAuth);
        } else {
            log.debug(entityDisplayPermission + " does not authorize " + whatToAuth);
        }
    
        return result;
    }
    
    static boolean isAuthorizedByEntityUpdatePermission(IdentifierBundle ac_subject, AccessObject whatToAuth, EntityAccessRule entityPermission, AccessOperation operation) {
        boolean isAuthorized = false;
        List<String> personUris = new ArrayList<String>(HasAssociatedIndividual.getIndividualUris(ac_subject));
        final AccessObjectType type = whatToAuth.getType();

        if (AccessObjectType.DATA_PROPERTY_STMT.equals(type)) {
            // Check resource
            String subjectUri = whatToAuth.getStatementSubject();
            if (isModifiable(subjectUri)) {
                Property predicate = whatToAuth.getPredicate();
                if (isModifiable(predicate.getURI())) {
                    isAuthorized = isAuthorizedForByEntityPermission(predicate, entityPermission);
                }
            }
    
            if (isAuthorized) {
                isAuthorized = isAuthorizedForByEntityPermission(whatToAuth, personUris, entityPermission);
            }
        } else if (AccessObjectType.OBJECT_PROPERTY_STMT.equals(type)) {
            String subjectUri = whatToAuth.getStatementSubject();
            String objectUri = whatToAuth.getStatementObject();
            if (isModifiable(subjectUri) && isModifiable(objectUri)) {
                Property predicate = whatToAuth.getPredicate();
                if (isModifiable(predicate.getURI())) {
                    isAuthorized = isAuthorizedForByEntityPermission(predicate, entityPermission);
                }
            }
    
            if (isAuthorized) {
                isAuthorized = isAuthorizedForByEntityPermission(whatToAuth, personUris, entityPermission);
            }
        } 
    
        if (isAuthorized) {
            log.debug(entityPermission + " authorizes " + whatToAuth);
        } else {
            log.debug(entityPermission + " does not authorize " + whatToAuth);
        }
    
        return isAuthorized;
    }

    static boolean isAuthorizedByBrokenPermission() {
        return false;
    }

    public static boolean isAuthorizedPermission(AuthorizationRequest ar , AccessRule rule) {
        IdentifierBundle ac_subject = ar.getIds();
        AccessObject whatToAuth = ar.getAccessObject();
        AccessOperation operation = ar.getAccessOperation();
        if (rule instanceof EntityAccessRule) {
            if (AccessOperation.DISPLAY.equals(operation)){
                return isAuthorizedByEntityDisplayPermission(whatToAuth, (EntityAccessRule) rule, operation);    
            }
            if (AccessOperation.UPDATE.equals(operation)){
                return isAuthorizedByEntityUpdatePermission(ac_subject, whatToAuth, (EntityAccessRule) rule, operation);
            }
            if (AccessOperation.PUBLISH.equals(operation)){
                return isAuthorizedByEntityPublishPermission(whatToAuth, (EntityAccessRule) rule, operation);
            }
        }
        return rule.match(ar);
    }
}
