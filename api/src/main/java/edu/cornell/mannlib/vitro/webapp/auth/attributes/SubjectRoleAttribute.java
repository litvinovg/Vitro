package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import java.util.List;

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
        final List<String> inputValues = ar.getRoleUris();
        if (AttributeValueTester.test(this,  inputValues.toArray(new String[0]))) {
            log.debug("Attribute match requested '" + inputValues + "'");
            return true;
        }
        log.debug("Attribute don't match requested '" + inputValues + "'");
        return false;
    }

    @Override
    public AttributeType getAttributeType() {
        return AttributeType.SUBJECT_ROLE_URI;
    }

}
