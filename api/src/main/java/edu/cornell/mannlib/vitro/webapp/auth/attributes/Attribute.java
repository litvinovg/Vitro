package edu.cornell.mannlib.vitro.webapp.auth.attributes;

import java.util.Set;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessOperation;

public interface Attribute {

    public boolean match(IdentifierBundle ac_subject, AccessObject whatToAuth, AccessOperation operation);
    
    public Set<AttributeGroup> getGroups();

    public AttributeType getType();
}
