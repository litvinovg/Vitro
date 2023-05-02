/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;
import edu.cornell.mannlib.vitro.webapp.beans.Property;

/**
 * A base class for RequestedAction that permits boolean operations on them.
 *
 * A null request is ignored, so in "and" it is equivalent to true, while in
 * "or" it is equivalent to false.
 */
public abstract class AccessObject {

	public static String ACTION_NAMESPACE = "java:";
    public static String SOME_URI = "?SOME_URI";
    public static Property SOME_PREDICATE = new Property(SOME_URI);
    public static String SOME_LITERAL = "?SOME_LITERAL";
    
    @Deprecated
    protected DecisionResult predefinedDecision = DecisionResult.INCONCLUSIVE;

    //public abstract PolicyContract getPolicyContract();
    
    /**
     * In its most basic form, a RequestAction needs to have an identifier.
     * Sometimes this will be enough.
     */
    public String getURI() {
    	return ACTION_NAMESPACE + this.getClass().getName();
    }

    @Override
    public String toString() {
    	return this.getClass().getSimpleName();
    }

}
