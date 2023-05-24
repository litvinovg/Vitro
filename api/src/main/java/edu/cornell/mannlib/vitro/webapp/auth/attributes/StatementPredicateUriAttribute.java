package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.objects.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.policy.PermissionsPolicy;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;

public class StatementPredicateUriAttribute extends AbstractAttribute {

    private static final Log log = LogFactory.getLog(PermissionsPolicy.class);

    public StatementPredicateUriAttribute(String uri, String objectUri) {
        super(uri, objectUri);
    }

    @Override
    public boolean match(AuthorizationRequest ar) {
        AccessObject ao = ar.getAccessObject();
        final String inputValue = ao.getStatementPredicateUri();
        if (AttributeValueTester.test(this, inputValue)) {
            log.debug("Attribute value match requested statement predicate uri '" + inputValue + "'");
            return true;
        }
        log.debug("Attribute value don't match requested statement predicate uri '" + inputValue + "'");
        return false;
    }

    @Override
    public AttributeType getAttributeType() {
        return AttributeType.STATEMENT_PREDICATE_URI;
    }

}