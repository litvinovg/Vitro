package edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessObjectType;
import edu.cornell.mannlib.vitro.webapp.auth.objects.AccessObject;
import edu.cornell.mannlib.vitro.webapp.beans.DataProperty;

public class DataPropertyAccessObject extends AccessObject {

    protected final DataProperty dataProperty;

    public DataPropertyAccessObject(DataProperty dataProperty) {
        super();
        this.dataProperty = dataProperty;
    }

    public DataProperty getDataProperty() {
    	return dataProperty;
    }

    @Override
    public String toString() {
    	return "DisplayDataProperty[" + dataProperty + "]";
    }

    @Override
    public AccessObjectType getType() {
        return AccessObjectType.DATA_PROPERTY;
    }
}