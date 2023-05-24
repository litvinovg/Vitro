package edu.cornell.mannlib.vitro.webapp.auth.policy;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyDecision;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyIface;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;

public interface Policies {

    public PolicyDecision decide(AuthorizationRequest ar);
    
    boolean contains(PolicyIface policy);

    void add(PolicyIface policy);

    void clear();

}