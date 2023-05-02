/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.Attribute;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessOperation;

/**
 * Interface that describes a unit of authorization, or permission to perform
 * requested actions.
 */
public abstract class AccessRule {
    AccessOperation operation;
    protected Set<Attribute> attributes = new HashSet<>();
    
	public Set<Attribute> getAttributes() {
        return attributes;
    }

    public abstract String getUri();

    public abstract void setUri(String uri);
	
	public void addAttribute(Attribute attr) {
	    attributes.add(attr);
	}

    public void setOperation(AccessOperation operation) {
        this.operation = operation;
    }

    public AccessOperation getOperation() {
        return operation;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccessRule)) {
            return false;
        }
        if (object == this) {
            return true;
        }
        AccessRule compared = (AccessRule) object;

        return new EqualsBuilder()
                .append(getAttributes(), compared.getAttributes())
                .append(getOperation(), compared.getOperation())
                .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(15, 101)
                .append(getAttributes())
                .append(getOperation())
                .toHashCode();
    }


}
