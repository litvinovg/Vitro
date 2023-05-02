package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleAccessRules {

    static final String NS = "java:edu.cornell.mannlib.vitro.webapp.auth.permissions.SimplePermission#";
    
    private static final Map<String, AccessRule> simpleAccessRules = new HashMap<String, AccessRule>();

    public static List<AccessRule> getAllInstances() {
        return new ArrayList<AccessRule>(simpleAccessRules.values());
    }
    
    public static void add(AccessRule permission) {
        if (SimpleAccessRules.contains(permission.getUri())) {
            throw new IllegalStateException("A SimplePermission named '" + permission.getUri() + "' already exists.");
        }
        simpleAccessRules.put(permission.getUri(), permission);
    }
    
    public static boolean contains(String uri) {
        return simpleAccessRules.containsKey(uri);
    }
}
