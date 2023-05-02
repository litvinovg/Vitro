package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.policy.PermissionsPolicy;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessOperation;

public class NamedObjectUriAttribute extends PlainAttribute {

    private static final Log log = LogFactory.getLog(PermissionsPolicy.class);

    public NamedObjectUriAttribute(String uri) {
        super(AttributeType.NAMED_OBJECT_URI, uri);
    }

    @Override
    public boolean match(IdentifierBundle ac_subject, AccessObject ao, AccessOperation operation) {
        if (getType().equals(ao.getType()) && 
            getUri().equals(ao.getUri())) {
            log.debug("Attribute " + getType() + " '" + getUri() + "' matched requested " + ao.getType() + " '" + ao.getUri() + "'");
            return true;
        }
        log.debug("Attribute " + getType() + " '" + getUri() + "' not matched requested " + ao.getType() + " '" + ao.getUri() + "'");
        return false;
    }
    
}
