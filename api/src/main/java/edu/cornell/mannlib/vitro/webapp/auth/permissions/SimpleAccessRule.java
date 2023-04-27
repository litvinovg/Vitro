/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.NamedAccessObject;

/**
 * A class of simple permissions. Each instance holds a RequestedAction, and
 * will only authorize that RequestedAction (or one with the same URI).
 */
public class SimpleAccessRule extends AccessRule {

    //Access object named action for compatibility reasons.
    //TODO:fix the name after merged into the code base
	public final AccessObject ACTION;

	public SimpleAccessRule(String uri) {
		super(uri);
	    this.ACTION = new NamedAccessObject(uri);
		SimpleAccessRules.add(this);
	}

}
