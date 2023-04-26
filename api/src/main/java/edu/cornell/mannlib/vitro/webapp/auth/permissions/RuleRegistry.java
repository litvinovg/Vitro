package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import java.util.HashSet;
import java.util.Set;

public class RuleRegistry {
    private static RuleRegistry INSTANCE = new RuleRegistry();

    private RuleRegistry() {
        INSTANCE = this;
    }

    public static RuleRegistry getInstance() {
        return INSTANCE;
    }
    
    Set<Rule> rules = new HashSet<>();
    
    public void addRule(Rule rule) {
        
    }
}
