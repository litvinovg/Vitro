package edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessObjectType;
import edu.cornell.mannlib.vitro.webapp.auth.objects.AccessObject;
import edu.cornell.mannlib.vitro.webapp.beans.ObjectProperty;

public class ObjectPropertyAccessObject extends AccessObject {

    protected final ObjectProperty objectProperty;

    public ObjectPropertyAccessObject(ObjectProperty objectProperty) {
        super();
        this.objectProperty = objectProperty;
    }

    public ObjectProperty getObjectProperty() {
    	return objectProperty;
    }

    @Override
    public String toString() {
    	return "ObjectPropertyAccessObject[" + objectProperty + "]";
    }
    
    @Override
    public AccessObjectType getType() {
        return AccessObjectType.OBJECT_PROPERTY;
    }
}