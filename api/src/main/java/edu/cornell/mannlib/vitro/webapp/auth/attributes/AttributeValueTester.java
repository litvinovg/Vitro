package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AttributeValueTester {
    private static final Log log = LogFactory.getLog(AttributeValueTester.class);

    static boolean test(Attribute attr, String... values) {
        TestType testType = attr.getTestType();
        switch (testType) {
        case EQUALS:
            return equals(attr, values);
        case NOT_EQUALS:
            return !equals(attr, values);
        case ONE_OF:
            return contains(attr, values);
        case NOT_ONE_OF:
            return !contains(attr, values);
        case STARTS_WITH:
            return startsWith(attr, values);
        default: 
            return false;
        }
    }

    private static boolean contains(Attribute attr, String... inputValues) {
        final Set<String> values = attr.getValues();
        for (String inputValue : inputValues) {
            if(values.contains(inputValue)){
                return true;
            }
        }
        return false;
    }

    private static boolean equals(Attribute attr, String... inputValues) {
        Set<String> values = attr.getValues();
        final int valuesSize = values.size();
        if(valuesSize != 1) {
            return false;
        }
        String value = values.iterator().next();
        for (String inputValue : inputValues) {
            if(value.equals(inputValue)){
                return true;
            }
        }
        return false;
    }
    
    private static boolean startsWith(Attribute attr, String... inputValues) {
        Set<String> values = attr.getValues();
        final int valuesSize = values.size();
        if(valuesSize != 1) {
            return false;
        }
        String value = values.iterator().next();
        for (String inputValue : inputValues) {
            if(inputValue != null && inputValue.startsWith(value)){
                return true;
            }
        }
        return false;
    }

}
