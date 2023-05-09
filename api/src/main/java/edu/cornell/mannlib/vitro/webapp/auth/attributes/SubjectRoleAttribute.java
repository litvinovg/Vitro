package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.policy.PermissionsPolicy;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;

public class SubjectRoleAttribute extends AbstractAttribute {

    private static final Log log = LogFactory.getLog(PermissionsPolicy.class);

    public SubjectRoleAttribute(String uri, String roleValue) {
        super(uri, roleValue);
    }

    @Override
    public boolean match(AuthorizationRequest ar) {
        if (ar.getRoleUris().contains(getValue())) {
            log.debug("Attribute '" + getValue() + "' match requested '" + ar.getRoleUris() + "'");
            return true;
        }
        log.debug("Attribute '" + getValue() + "' not match requested '" + ar.getRoleUris() + "'");
        return false;
    }

    @Override
    public AttributeType getAttributeType() {
        return AttributeType.SUBJECT_ROLE_URI;
    }

}
