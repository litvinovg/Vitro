/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyIface;

/**
 * A base class for RequestedAction that permits boolean operations on them.
 *
 * A null request is ignored, so in "and" it is equivalent to true, while in
 * "or" it is equivalent to false.
 */
public abstract class AuthorizationRequest {

	public abstract boolean isAuthorized(IdentifierBundle ids,	PolicyIface policy);

}
