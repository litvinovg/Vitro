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
        if (type.equals(AttributeType.SUBJECT_ROLE_URI)) {
            at = new SubjectRoleAttribute(attributeUri, value);
            return at;
        }
        if (type.equals(AttributeType.OPERATION)) {
            at = new OperationAttribute(attributeUri, value);
            return at;
        }
        if (type.equals(AttributeType.OBJECT_URI)) {
            at = new ObjectUriAttribute(attributeUri, value);
            return at;
        }
        if (type.equals(AttributeType.OBJECT_TYPE)) {
            at = new ObjectTypeAttribute(attributeUri, value);
            return at;
        }
        if (type.equals(AttributeType.SUBJECT_TYPE)) {
            at = new SubjectTypeAttribute(attributeUri, value);
            return at;
        }
        if (type.equals(AttributeType.STATEMENT_PREDICATE_URI)) {
            at = new ObjectTypeAttribute(attributeUri, value);
            return at;
        }
        throw new RuntimeException();
    }

    private static String getValue(QuerySolution qs) {
        String value = qs.getResource(AccessRuleStore.ATTR_VALUE).getURI();
        if (!qs.contains(AccessRuleStore.LITERAL_VALUE) ||
            !qs.get(AccessRuleStore.LITERAL_VALUE).isLiteral()) {
            return value;
        }
        value = qs.getLiteral(AccessRuleStore.LITERAL_VALUE).toString();
        return value;
    }

}
