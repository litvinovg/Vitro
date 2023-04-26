package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import java.util.HashSet;
import java.util.Set;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.Attribute;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;

public class Rule {
    public enum Operation { ACCESS, DISPLAY, UPDATE, PUBLISH };
    private Operation operation;
    private DecisionResult decision = DecisionResult.UNAUTHORIZED;
    private String role = "";
    private Set<Attribute> attributes = new HashSet<Attribute>();
    
    public Rule() {
        
    }

}
