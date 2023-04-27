package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleAccessRules {

    static final String NS = "java:edu.cornell.mannlib.vitro.webapp.auth.permissions.SimplePermission#";
    
    private static final Map<String, SimpleAccessRule> simpleAccessRules = new HashMap<String, SimpleAccessRule>();

    public static List<SimpleAccessRule> getAllInstances() {
        return new ArrayList<SimpleAccessRule>(simpleAccessRules.values());
    }
    
    public static void add(SimpleAccessRule permission) {
        if (SimpleAccessRules.contains(permission.getUri())) {
            throw new IllegalStateException("A SimplePermission named '" + permission.getUri() + "' already exists.");
        }
        simpleAccessRules.put(permission.getUri(), permission);
    }
    
    public static boolean contains(String uri) {
        return simpleAccessRules.containsKey(uri);
    }
}
