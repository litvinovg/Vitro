package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class AbstractAttribute implements Attribute {
    
    private String value;
    private String uri;
    private TestType testType = TestType.EQUALS;

    public AbstractAttribute(String uri, String value) {
        super();
        this.uri = uri;
        this.value = value;
    }
    
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public TestType getTestType() {
        return testType;
    }
    
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AbstractAttribute)) {
            return false;
        }
        if (object == this) {
            return true;
        }
        AbstractAttribute compared = (AbstractAttribute) object;
    
        return new EqualsBuilder()
                .append(getUri(), compared.getUri())
                .append(getValue(), compared.getValue())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(15, 101)
                .append(getUri())
                .append(getValue())
                .toHashCode();
    }
}
