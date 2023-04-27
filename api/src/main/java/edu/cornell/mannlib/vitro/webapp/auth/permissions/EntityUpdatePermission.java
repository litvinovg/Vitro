/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt.DataPropertyStatementAccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt.ObjectPropertyStatementAccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt.PropertyStatementAccessObject;
import edu.cornell.mannlib.vitro.webapp.beans.Property;
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.rdf.model.impl.Util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EntityUpdatePermission extends EntityPermission {
    private static final Log log = LogFactory.getLog(EntityUpdatePermission.class);

    private static final Collection<String> PROHIBITED_NAMESPACES = Arrays
            .asList(VitroVocabulary.vitroURI, "" );

    private static final Collection<String> PERMITTED_EXCEPTIONS = Arrays
            .asList(VitroVocabulary.MONIKER,
                    VitroVocabulary.MODTIME, VitroVocabulary.IND_MAIN_IMAGE,
                    VitroVocabulary.LINK, VitroVocabulary.PRIMARY_LINK,
                    VitroVocabulary.ADDITIONAL_LINK,
                    VitroVocabulary.LINK_ANCHOR, VitroVocabulary.LINK_URL );

    public EntityUpdatePermission(String uri) {
        super(uri);
    }

    @Override
    public boolean isAuthorized(List<String> personUris, AccessObject whatToAuth) {
        boolean isAuthorized = false;

        if (whatToAuth instanceof DataPropertyStatementAccessObject) {
            // Check resource
            String subjectUri = ((DataPropertyStatementAccessObject)whatToAuth).getSubjectUri();
            if (isModifiable(subjectUri)) {
                Property predicate = ((DataPropertyStatementAccessObject)whatToAuth).getPredicate();
                if (isModifiable(predicate.getURI())) {
                    isAuthorized = isAuthorizedFor(predicate);
                }
            }

            if (isAuthorized) {
                isAuthorized = isAuthorizedFor((PropertyStatementAccessObject) whatToAuth, personUris);
            }
        } else if (whatToAuth instanceof ObjectPropertyStatementAccessObject) {
            String subjectUri = ((ObjectPropertyStatementAccessObject)whatToAuth).getSubjectUri();
            String objectUri = ((ObjectPropertyStatementAccessObject)whatToAuth).getObjectUri();
            if (isModifiable(subjectUri) && isModifiable(objectUri)) {
                Property predicate = ((ObjectPropertyStatementAccessObject)whatToAuth).getPredicate();
                if (isModifiable(predicate.getURI())) {
                    isAuthorized = isAuthorizedFor(predicate);
                }
            }

            if (isAuthorized) {
                isAuthorized = isAuthorizedFor((PropertyStatementAccessObject) whatToAuth, personUris);
            }
        } 

        if (isAuthorized) {
            log.debug(this + " authorizes " + whatToAuth);
        } else {
            log.debug(this + " does not authorize " + whatToAuth);
        }

        return isAuthorized;
    }

    private boolean isModifiable(String uri) {
        if (PROHIBITED_NAMESPACES.contains(namespace(uri))) {
            if (PERMITTED_EXCEPTIONS.contains(uri)) {
                return true;
            } else {
                return false;
            }
        }

        return true;

    }

    private String namespace(String uri) {
        return uri.substring(0, Util.splitNamespaceXML(uri));
    }
}
