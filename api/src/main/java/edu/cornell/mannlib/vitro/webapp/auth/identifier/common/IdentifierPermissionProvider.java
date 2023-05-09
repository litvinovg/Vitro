/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.identifier.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.Identifier;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.rules.AccessRule;

/**
 * The current user has this Permission, through one or more PermissionSets.
 */
public class IdentifierPermissionProvider extends AbstractCommonIdentifier implements Identifier {
	private static Collection<IdentifierPermissionProvider> getIdentifiers(IdentifierBundle ids) {
		return getIdentifiersForClass(ids, IdentifierPermissionProvider.class);
	}

	public static Collection<AccessRule> getPermissions(IdentifierBundle ids) {
		Set<AccessRule> set = new HashSet<AccessRule>();
		for (IdentifierPermissionProvider id : getIdentifiers(ids)) {
			set.add(id.getPermission());
		}
		return set;
	}

	private final AccessRule permission; // never null

	public IdentifierPermissionProvider(AccessRule permission) {
		if (permission == null) {
			throw new NullPointerException("permission may not be null.");
		}
		this.permission = permission;
	}

	public AccessRule getPermission() {
		return permission;
	}

	@Override
	public String toString() {
		return "HasPermission[" + permission + "]";
	}

	@Override
	public int hashCode() {
		return permission.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof IdentifierPermissionProvider)) {
			return false;
		}
		IdentifierPermissionProvider that = (IdentifierPermissionProvider) obj;
		return this.permission.equals(that.permission);
	}

}
