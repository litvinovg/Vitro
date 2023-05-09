package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.policy.PermissionsPolicy;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;

public class OperationAttribute extends AbstractAttribute {

    private static final Log log = LogFactory.getLog(PermissionsPolicy.class);

    public OperationAttribute(String uri, String roleValue) {
        super(uri, roleValue);
    }

    @Override
    public boolean match(AuthorizationRequest ar) {
         AccessOperation aop = ar.getAccessOperation();
        if (getValue().equals(aop.toString())) {
            log.debug("Attribute '" + getValue() + "' match requested operation '" + aop.toString() + "'");
            return true;
        }
        log.debug("Attribute '" + getValue() + "' not match requested operation '" + aop.toString() + "'");
        return false;
    }

    @Override
    public AttributeType getAttributeType() {
        return AttributeType.OPERATION;
    }

}
