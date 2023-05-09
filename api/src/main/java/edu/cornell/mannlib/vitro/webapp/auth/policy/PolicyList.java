package edu.cornell.mannlib.vitro.webapp.auth.policy;

import java.util.ListIterator;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyDecision;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyIface;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;

public interface PolicyList {

    public PolicyDecision decide(AuthorizationRequest ar);
    
    boolean contains(PolicyIface policy);

    void add(PolicyIface policy);

    void add(int i, PolicyIface policy);

    ListIterator<PolicyIface> listIterator();

    void clear();

}