package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.objects.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.policy.PermissionsPolicy;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;
import edu.cornell.mannlib.vitro.webapp.beans.ObjectProperty;

public class ObjectPropertyUriAttribute extends AbstractAttribute {

    private static final Log log = LogFactory.getLog(PermissionsPolicy.class);

    public ObjectPropertyUriAttribute(String uri, String objectUri) {
        super(uri, objectUri);
    }

    @Override
    public boolean match(AuthorizationRequest ar) {
        AccessObject ao = ar.getAccessObject();
        ObjectProperty objectProperty = ao.getObjectProperty();
        String uri = objectProperty.getURI();
        if (objectProperty != null) {
            uri = objectProperty.getURI();
        }
        if (getValue().equals(uri)) {
            log.debug("Attribute value '" + getValue() + "' matched requested statement object property uri '" + uri + "'");
            return true;
        }
        log.debug("Attribute value '" + getValue() + "' not matched requested statement object property uri '" + uri + "'");
        return false;
    }

    @Override
    public AttributeType getAttributeType() {
        return AttributeType.OBJECT_PROPERTY_URI;
    }

}
