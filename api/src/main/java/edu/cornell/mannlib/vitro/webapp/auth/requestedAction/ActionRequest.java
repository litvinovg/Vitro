/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.policy.PolicyHelper;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyIface;
import edu.cornell.mannlib.vitro.webapp.beans.Property;

/**
 * A base class for RequestedAction that permits boolean operations on them.
 *
 * A null request is ignored, so in "and" it is equivalent to true, while in
 * "or" it is equivalent to false.
 */
public abstract class ActionRequest {

	public static String ACTION_NAMESPACE = "java:";
    public static String SOME_URI = "?SOME_URI";
    public static Property SOME_PREDICATE = new Property(SOME_URI);
    public static String SOME_LITERAL = "?SOME_LITERAL";

    /**
     * In its most basic form, a RequestAction needs to have an identifier.
     * Sometimes this will be enough.
     */
    public String getURI() {
    	return ACTION_NAMESPACE + this.getClass().getName();
    }

    /**
     * For authorization, just ask the Policy. INCONCLUSIVE is not good enough.
     */
    public boolean isAuthorized(IdentifierBundle ids, PolicyIface policy) {
        return PolicyHelper.actionRequestIsAuthorized( ids,  policy,  this);
    }

    @Override
    public String toString() {
    	return this.getClass().getSimpleName();
    }

}
