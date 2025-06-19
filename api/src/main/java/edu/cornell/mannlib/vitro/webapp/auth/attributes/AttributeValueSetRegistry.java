package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AttributeValueSetRegistry {

    private static AttributeValueSetRegistry INSTANCE = new AttributeValueSetRegistry();
    private static final Log log = LogFactory.getLog(AttributeValueSetRegistry.class);
    private Map<AttributeValueKey, AttributeValueSet> valueKeyMap = new ConcurrentHashMap<>();
    private Map<String, AttributeValueSet> uriMap = new ConcurrentHashMap<>();

    private AttributeValueSetRegistry() {
        INSTANCE = this;
    }

    public static AttributeValueSetRegistry getInstance() {
        return INSTANCE;
    }

    public AttributeValueSet get(AttributeValueKey key) {
        return valueKeyMap.get(key);
    }

    public AttributeValueSet get(String uri) {
        return uriMap.get(uri);
    }

    public void put(AttributeValueKey key, AttributeValueSet values) {
        valueKeyMap.put(key, values);
        uriMap.put(values.getValueSetUri(), values);
    }

    public void clear() {
        valueKeyMap.clear();
    }
}
