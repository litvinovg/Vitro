package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.objects.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;

public class ObjectTypeAttribute extends AbstractAttribute {

    private static final Log log = LogFactory.getLog(ObjectTypeAttribute.class);

    public ObjectTypeAttribute(String uri, String roleValue) {
        super(uri, roleValue);
    }

    @Override
    public boolean match(AuthorizationRequest ar) {
         AccessObject ao = ar.getAccessObject();
        if (getValue().equals(ao.getType().toString())) {
            log.debug("Attribute '" + getValue() + "' match requested object type '" + ao.getType().toString() + "'");
            return true;
        }
        log.debug("Attribute '" + getValue() + "' not match requested object type '" + ao.getType().toString() + "'");
        return false;
    }

    @Override
    public AttributeType getAttributeType() {
        return AttributeType.SUBJECT_TYPE;
    }
}
