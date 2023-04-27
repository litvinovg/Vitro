/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.NamedAccessObject;

/**
 * A class of simple permissions. Each instance holds a RequestedAction, and
 * will only authorize that RequestedAction (or one with the same URI).
 */
public class SimplePermission extends Permission {

	public final AccessObject actionRequest;

	public SimplePermission(String uri) {
		super(uri);
	    this.actionRequest = new NamedAccessObject(uri);
		SimplePermissions.add(this);
	}

}
