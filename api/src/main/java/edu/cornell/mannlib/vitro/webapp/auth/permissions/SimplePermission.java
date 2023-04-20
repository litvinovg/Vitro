/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.ActionRequest;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.SimpleActionReqiest;

/**
 * A class of simple permissions. Each instance holds a RequestedAction, and
 * will only authorize that RequestedAction (or one with the same URI).
 */
public class SimplePermission extends Permission {
	private static final Log log = LogFactory.getLog(SimplePermission.class);

	public final ActionRequest actionRequest;

	public SimplePermission(String uri) {
		super(uri);
		
		if (SimplePermissions.contains(this.uri)) {
			throw new IllegalStateException("A SimplePermission named '" + this.uri + "' already exists.");
		}
	    this.actionRequest = new SimpleActionReqiest(uri);

		SimplePermissions.add(this);
	}

	@Override
	public boolean isAuthorized(List<String> personUris, ActionRequest whatToAuth) {
		return isAuthorized(whatToAuth);
	}

    private boolean isAuthorized(ActionRequest whatToAuth) {
        if (whatToAuth != null) {
			if (getUri().equals(whatToAuth.getURI())) {
				log.debug(this + " authorizes " + whatToAuth);
				return true;
			}
		}
		log.debug(this + " does not authorize " + whatToAuth);
		return false;
    }

	@Override
	public String toString() {
		return "SimplePermission['" + uri+ "']";
	}

}
