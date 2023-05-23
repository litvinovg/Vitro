package edu.cornell.mannlib.vitro.webapp.auth.rules;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.query.QuerySolution;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AttributeFactory;

public class AccessRuleFactory {

    private static final Log log = LogFactory.getLog(AccessRuleFactory.class);

    public static AccessRule createRule(QuerySolution qs) {
        AccessRule ar = new SimpleAccessRule();
        try {
            ar.setRuleUri(qs.getResource(AccessRuleStore.RULE).getURI());
            ar.addAttribute(AttributeFactory.createAttribute(qs));
        } catch (Exception e) {
            log.error(e, e);
        }
        return ar;
    }
}
