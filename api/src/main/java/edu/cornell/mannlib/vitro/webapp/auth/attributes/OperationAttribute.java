package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;

public class OperationAttribute extends AbstractAttribute {

    private static final Log log = LogFactory.getLog(OperationAttribute.class);

    public OperationAttribute(String uri, String roleValue) {
        super(uri, roleValue);
    }

    @Override
    public boolean match(AuthorizationRequest ar) {
        AccessOperation aop = ar.getAccessOperation();
        final String inputValue = aop.toString();
        if (AttributeValueTester.test(this, inputValue)) {
            log.debug("Attribute match requested operation '" + inputValue + "'");
            return true;
        }
        log.debug("Attribute don't match requested operation '" + inputValue + "'");
        return false;
    }

    @Override
    public AttributeType getAttributeType() {
        return AttributeType.OPERATION;
    }

}
