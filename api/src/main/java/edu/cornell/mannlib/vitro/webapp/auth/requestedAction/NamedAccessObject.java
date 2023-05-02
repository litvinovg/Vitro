/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AttributeType;

/**
 * A RequestedAction that can be recognized by a SimplePermission.
 */
public class NamedAccessObject extends AccessObject {
	private final String uri;
	private final AttributeType type = AttributeType.NAMED_OBJECT_URI;

	public NamedAccessObject(String uri) {
		if (uri == null) {
			throw new NullPointerException("uri may not be null.");
		}

		this.uri = uri;
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public int hashCode() {
		return uri.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof NamedAccessObject) {
			NamedAccessObject that = (NamedAccessObject) o;
			return equivalent(this.uri, that.uri);
		}
		return false;
	}

	private boolean equivalent(Object o1, Object o2) {
		return (o1 == null) ? (o2 == null) : o1.equals(o2);
	}

	@Override
	public String toString() {
		return "SimpleRequestedAction['" + uri + "']";
	}

    @Override
    public AttributeType getType() {
        return type;
    }

}
