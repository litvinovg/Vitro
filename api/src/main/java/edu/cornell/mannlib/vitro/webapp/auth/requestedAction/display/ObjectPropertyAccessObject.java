package edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject;
import edu.cornell.mannlib.vitro.webapp.beans.ObjectProperty;

public abstract class ObjectPropertyAccessObject extends AccessObject {

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

}