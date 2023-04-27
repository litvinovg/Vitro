/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.NamedAccessObject;

/**
 * A class of simple permissions. Each instance holds a RequestedAction, and
 * will only authorize that RequestedAction (or one with the same URI).
 */
public class SimplePermission extends Permission {
	static final Log log = LogFactory.getLog(SimplePermission.class);

	public final AccessObject actionRequest;

	public SimplePermission(String uri) {
		super(uri);
		if (SimplePermissions.contains(this.uri)) {
			throw new IllegalStateException("A SimplePermission named '" + this.uri + "' already exists.");
		}
	    this.actionRequest = new NamedAccessObject(uri);

		SimplePermissions.add(this);
	}

	@Override
	public boolean isAuthorized(List<String> personUris, AccessObject whatToAuth) {
		return EntityPermissionHelper.isAuthorizedBySimplePermission(whatToAuth, this);
	}

}
