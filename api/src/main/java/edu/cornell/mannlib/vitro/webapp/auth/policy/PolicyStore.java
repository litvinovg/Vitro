/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.policy;

import java.util.ListIterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyIface;

/**
 * This is a List of Policy Objects that implements PolciyIface.  The intent
 * is to make it easy to query a list of policies for a PolicyDecision.
 *
 *  The Policy objects in the PolicyList are queried for authorization in order
 *  and return the first AUTHORIZED or UNAUTHROIZED decision.  INCONCLUSIVE
 *  or null decisions will be ignored and the next policy on the list will
 *  be queried.
 */
public class PolicyStore extends PolicyListImpl{
    
    private static final Log log = LogFactory.getLog(PolicyStore.class.getName());

    private static PolicyStore INSTANCE = new PolicyStore();

    private PolicyStore() {
        INSTANCE = this;
    }
    
    public static PolicyStore getInstance() {
        return INSTANCE;
    }
    
    /**
     * Add the policy to the end of the list.
     */
    public static void addPolicy(PolicyIface policy) {
    	if (policy == null) {
    		return;
    	}
    	PolicyList policies = getInstance();
    	if (!policies.contains(policy)) {
    		policies.add(policy);
    		log.debug("Added policy: " + policy.toString());
    	} else {
    		log.warn("Ignored attempt to add redundant policy.");
    	}
    }

    /**
     * Add the policy to the front of the list. It may be moved further down the
     * list by other policies that are later added using this method.
     */
    public static void addPolicyAtFront(PolicyIface policy) {
    	if (policy == null) {
    		return;
    	}
    	PolicyList policies = getInstance();
    	if (!policies.contains(policy)) {
    		policies.add(0, policy);
    		log.debug("Added policy at front: " + policy.toString());
    	} else {
    		log.warn("Ignored attempt to add redundant policy.");
    	}
    }

    /**
     * Replace the first instance of this class of policy in the list. If no
     * instance is found, add the policy to the end of the list.
     */
    public static void replacePolicy(PolicyIface policy) {
    	if (policy == null) {
    		return;
    	}
    
    	Class<?> clzz = policy.getClass();
    	PolicyList policies = getInstance();
    	ListIterator<PolicyIface> it = policies.listIterator();
    	while (it.hasNext()) {
    		if (clzz.isAssignableFrom(it.next().getClass())) {
    			it.set(policy);
    			return;
    		}
    	}
    
    	addPolicy(policy);
    } 
}
