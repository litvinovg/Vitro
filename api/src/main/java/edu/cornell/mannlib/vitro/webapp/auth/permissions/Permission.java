/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject;

import java.util.List;

/**
 * Interface that describes a unit of authorization, or permission to perform
 * requested actions.
 */
public abstract class Permission implements Comparable<Permission> {
	protected final String uri;
	protected Permission(String uri) {
		if (uri == null) {
			throw new NullPointerException("uri may not be null.");
		}
		this.uri = uri;
	}

	/**
	 * Get the URI that identifies this Permission object.
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Is a user with this Permission authorized to perform this
	 * RequestedAction?
	 *
	 * @param personUris Any Uris of people in the data that are associated with this user. May be null / empty.
	 * @param whatToAuth The action to authorise
	 */
	public abstract boolean isAuthorized(List<String> personUris, AccessObject whatToAuth);

	@Override
	public int compareTo(Permission that) {
		return this.uri.compareTo(that.uri);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		Permission that = (Permission) obj;
		return this.uri.equals(that.uri);
	}

	@Override
	public int hashCode() {
		return uri.hashCode();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "['" + uri + "']";
	}

}
