package edu.cornell.mannlib.vitro.webapp.auth.rules;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.query.QuerySolution;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessObjectType;
import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessOperation;
import edu.cornell.mannlib.vitro.webapp.auth.attributes.AttributeFactory;

public class AccessRuleFactory {

    private static final Log log = LogFactory.getLog(AccessRuleFactory.class);

    static AccessRule createRule(QuerySolution qs) {
        AccessRule ar = new SimpleAccessRule();
        try {
            ar.setRuleUri(qs.getResource(AccessRuleStore.RULE).getURI());
            //ar.setOperation(getOperationById(qs));  
            //ar.setObjectType(getObjectType(qs));
            //ar.setObjectUri(getObjectUri(qs));
            ar.addAttribute(AttributeFactory.createAttribute(qs));
        } catch (Exception e) {
            log.error(e, e);
        }
        return ar;
    }

    private static AccessOperation getOperationById(QuerySolution qs) {
        String operationId = qs.getLiteral(AccessRuleStore.OPERATION_ID).getString();
        return AccessOperation.valueOf(operationId.trim().toUpperCase());
    }

    private static String getObjectUri(QuerySolution qs) {
        String objectUri = "";
        if (!qs.contains(AccessRuleStore.OBJECT_URI) || !qs.get(AccessRuleStore.OBJECT_URI).isResource()) {
            return objectUri;
        }
        objectUri = qs.getResource(AccessRuleStore.OBJECT_URI).getURI();
        return objectUri;
    }

    private static AccessObjectType getObjectType(QuerySolution qs) {
        String typeId = qs.getLiteral(AccessRuleStore.OBJECT_TYPE_ID).getString();
        AccessObjectType type = AccessObjectType.valueOf(typeId.trim().toUpperCase());
        return type;
    }

}
