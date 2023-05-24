package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.common.IsRootUser;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;

public class SubjectTypeAttribute extends AbstractAttribute {

    private static final Log log = LogFactory.getLog(SubjectTypeAttribute.class);

    public SubjectTypeAttribute(String uri, String subjectTypeValue) {
        super(uri, subjectTypeValue);
    }

    @Override
    public boolean match(AuthorizationRequest ar) {
        IdentifierBundle ac_subject = ar.getIds();
        String inputValue = IsRootUser.isRootUser(ac_subject) ? "ROOT_USER" : "";
        if (AttributeValueTester.test(this, inputValue)) {
            log.debug("Attribute subject type match requested object type '");
            return true;
        } else {
            log.debug("Attribute subject type don't match requested object type '");
            return false;
        }    
    }

    @Override
    public AttributeType getAttributeType() {
        return AttributeType.OBJECT_TYPE;
    }
}