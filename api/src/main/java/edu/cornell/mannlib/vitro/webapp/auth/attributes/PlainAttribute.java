package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class PlainAttribute implements Attribute {
    
    private AttributeType type;
    private String uri;
    private Set<AttributeGroup> groups = Collections.emptySet();
    
    public PlainAttribute(AttributeType type, String uri) {
        super();
        this.type = type;
        this.uri = uri;
    }

    public void addGroup(AttributeGroup ag) {
        if (groups.isEmpty()) {
            groups = new HashSet<>();
        }
        groups.add(ag);
    }
    
    public String getUri() {
        return uri;
    }

    @Override
    public AttributeType getType() {
        return type;
    }

    @Override
    public Set<AttributeGroup> getGroups() {
        return groups;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PlainAttribute)) {
            return false;
        }
        if (object == this) {
            return true;
        }
        PlainAttribute compared = (PlainAttribute) object;
    
        return new EqualsBuilder()
                .append(getType(), compared.getType())
                .append(getUri(), compared.getUri())
                .append(getGroups(), compared.getGroups())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(15, 101)
                .append(getType())
                .append(getUri())
                .append(getGroups())
                .toHashCode();
    }
}
