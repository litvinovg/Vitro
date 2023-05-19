package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import java.util.Set;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;

public interface Attribute {

    public void setUri(String uri);
    
    public String getUri();

    public boolean match(AuthorizationRequest ar);
    
    public AttributeType getAttributeType();
    
    public TestType getTestType();

    String getValue();
}
