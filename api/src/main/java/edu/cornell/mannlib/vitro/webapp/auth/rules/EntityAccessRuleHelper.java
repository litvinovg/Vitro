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
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary;
import edu.cornell.mannlib.vitro.webapp.utils.RelationshipChecker;

public class EntityAccessRuleHelper {

    static final Log log = LogFactory.getLog(EntityAccessRuleHelper.class);

    
    static boolean isAuthorizedBySparqlQuery(AccessObject ao, List<String> personUris, EntityAccessRule entityPermission) {
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
        String objectUri = null;
        final AccessObjectType type = ao.getType();
        if (AccessObjectType.DATA_PROPERTY_STATEMENT.equals(type)) {
        } else if (AccessObjectType.OBJECT_PROPERTY_STATEMENT.equals(type)) {
            objectUri = ao.getStatementObject();
        }
        
        // If the subject or object is a user URI for the current user, authorize access
        for (String userUri : personUris) {
            if (ao.getStatementSubject() != null && ao.getStatementSubject().equals(userUri)) {
                return true;
            }
            if (objectUri != null && objectUri.equals(userUri)) {
                return true;
            }
        }
        
        return RelationshipChecker.anyRelated(ao.getStatementOntModel(), Arrays.asList(ao.getResourceUris()), personUris);
    }

    static boolean matches(String uri, EntityAccessRule entityPermission) {
        if (AccessObject.SOME_URI.equals(uri)) {
            return true;
        }
        return false;
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

    static boolean isAuthorizedByEntityPublishOrDisplayPermission(AccessObject ao, EntityAccessRule ear, AccessOperation operation) {
        boolean result = false;
        final AccessObjectType type = ao.getType();
        if (AccessObjectType.DATA_PROPERTY.equals(type)) {
            result = matches(ao.getDataProperty().getURI(), ear);
        } else if (AccessObjectType.OBJECT_PROPERTY.equals(type)) {
            result = matches(ao.getObjectProperty().getURI(), ear);
        } else if (AccessObjectType.DATA_PROPERTY_STATEMENT.equals(type)) {
            result = matches(ao.getStatementPredicateUri(), ear);
        } else if (AccessObjectType.OBJECT_PROPERTY_STATEMENT.equals(type)) {
            result = matches(ao.getStatementPredicateUri(), ear);
        }
        return result;
    }
    
    static boolean isAuthorizedByEntityUpdatePermission(IdentifierBundle ac_subject, AccessObject ao, EntityAccessRule ear, AccessOperation operation) {
        boolean isAuthorized = false;
        List<String> personUris = new ArrayList<String>(HasAssociatedIndividual.getIndividualUris(ac_subject));
        final AccessObjectType type = ao.getType();

        if (AccessObjectType.DATA_PROPERTY_STATEMENT.equals(type)) {
            // Check resource
            if (isModifiable(ao.getStatementSubject())) {
                if (isModifiable(ao.getStatementPredicateUri())) {
                    isAuthorized = matches(ao.getStatementPredicateUri(), ear) &&
                            isAuthorizedBySparqlQuery(ao, personUris, ear);
                }
            }
    
        } else if (AccessObjectType.OBJECT_PROPERTY_STATEMENT.equals(type)) {
            if (isModifiable(ao.getStatementSubject()) && isModifiable(ao.getStatementObject())) {
                if (isModifiable(ao.getStatementPredicateUri())) {
                    isAuthorized = matches(ao.getStatementPredicateUri(), ear) &&
                    isAuthorizedBySparqlQuery(ao, personUris, ear);
                }
            }
    
        } 
    
        if (isAuthorized) {
            log.debug(ear + " authorizes " + ao);
        } else {
            log.debug(ear + " does not authorize " + ao);
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
                return isAuthorizedByEntityPublishOrDisplayPermission(whatToAuth, (EntityAccessRule) rule, operation);    
            }
            if (AccessOperation.UPDATE.equals(operation)){
                return isAuthorizedByEntityUpdatePermission(ac_subject, whatToAuth, (EntityAccessRule) rule, operation);
            }
            if (AccessOperation.PUBLISH.equals(operation)){
                return isAuthorizedByEntityPublishOrDisplayPermission(whatToAuth, (EntityAccessRule) rule, operation);
            }
        }
        return rule.match(ar);
    }
}
