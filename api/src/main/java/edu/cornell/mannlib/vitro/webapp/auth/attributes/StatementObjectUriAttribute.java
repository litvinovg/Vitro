package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.objects.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.policy.PermissionsPolicy;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;

public class StatementObjectUriAttribute extends AbstractAttribute {

    private static final Log log = LogFactory.getLog(PermissionsPolicy.class);

    public StatementObjectUriAttribute(String uri, String objectUri) {
        super(uri, objectUri);
    }

    @Override
    public boolean match(AuthorizationRequest ar) {
        AccessObject ao = ar.getAccessObject();
        if (getValue().equals(ao.getStatementObject())) {
            log.debug("Attribute value '" + getValue() + "' matched requested statement object uri '" + ao.getStatementObject() + "'");
            return true;
        }
        log.debug("Attribute value '" + getValue() + "' not matched requested statement object uri '" + ao.getStatementObject() + "'");
        return false;
    }

    @Override
    public AttributeType getAttributeType() {
        return AttributeType.STATEMENT_OBJECT_URI;
    }

}
