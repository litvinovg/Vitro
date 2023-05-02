package edu.cornell.mannlib.vitro.webapp.auth.policy;

import java.util.ListIterator;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyDecision;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyIface;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessOperation;

public interface PolicyList {

    public PolicyDecision decide(IdentifierBundle whoToAuth, AccessObject whatToAuth, AccessOperation operation);
    
    boolean contains(PolicyIface policy);

    void add(PolicyIface policy);

    void add(int i, PolicyIface policy);

    ListIterator<PolicyIface> listIterator();

    void clear();

}