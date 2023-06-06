package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import org.apache.jena.query.QuerySolution;

import edu.cornell.mannlib.vitro.webapp.auth.rules.AccessRuleStore;

public class AttributeFactory {

    public static Attribute createAttribute(QuerySolution qs) {
        String typeId = qs.getLiteral(AccessRuleStore.TYPE_ID).getString();
        String attributeUri = qs.getResource(AccessRuleStore.ATTRIBUTE).getURI();
        AttributeType type = AttributeType.valueOf(typeId);
        String testId = qs.getLiteral("testId").getString();
        String value = getValue(qs);

        Attribute at = null;
        switch (type) {
        case SUBJECT_ROLE_URI:
            at = new SubjectRoleAttribute(attributeUri, value);
            break;
        case OPERATION:
            at = new OperationAttribute(attributeUri, value);
            break;
        case OBJECT_URI:
            at = new ObjectUriAttribute(attributeUri, value);
            break;
        case OBJECT_TYPE:
            at = new ObjectTypeAttribute(attributeUri, value);
            break;
        case SUBJECT_TYPE:
            at = new SubjectTypeAttribute(attributeUri, value);
            break;
        case STATEMENT_PREDICATE_URI:
            at = new StatementPredicateUriAttribute(attributeUri, value);
            break;
        case STATEMENT_SUBJECT_URI:
            at = new StatementSubjectUriAttribute(attributeUri, value);
            break;
        case STATEMENT_OBJECT_URI:
            at = new StatementObjectUriAttribute(attributeUri, value);
            break;
        default :
            at = null;
        }    
        at.setTestType(TestType.valueOf(testId));
        return at;
    }

    private static String getValue(QuerySolution qs) {
        if (!qs.contains(AccessRuleStore.LITERAL_VALUE) ||
            !qs.get(AccessRuleStore.LITERAL_VALUE).isLiteral()) {
            String value = qs.getResource(AccessRuleStore.ATTR_VALUE).getURI();
            return value;
        } else {
            String value = qs.getLiteral(AccessRuleStore.LITERAL_VALUE).toString();
            return value;    
        }
    }
 
    public static void extendAttribute(Attribute attribute, QuerySolution qs) throws Exception {
        String testId = qs.getLiteral("testId").getString();
        if (TestType.ONE_OF.toString().equals(testId) ||
            TestType.NOT_ONE_OF.toString().equals(testId)) {
            attribute.addValue(getValue(qs));
            return;
        }
        throw new Exception();
    }
}