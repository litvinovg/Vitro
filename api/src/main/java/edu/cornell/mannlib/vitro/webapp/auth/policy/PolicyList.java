package edu.cornell.mannlib.vitro.webapp.auth.policy;

import java.util.ListIterator;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyIface;

public interface PolicyList extends PolicyIface {

    boolean contains(PolicyIface policy);

    void add(PolicyIface policy);

    void add(int i, PolicyIface policy);

    ListIterator<PolicyIface> listIterator();

    void clear();

}