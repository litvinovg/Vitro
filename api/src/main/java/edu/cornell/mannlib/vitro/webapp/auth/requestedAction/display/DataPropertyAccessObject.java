package edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AttributeType;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject;
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
    public AttributeType getType() {
        return AttributeType.DATA_PROPERTY;
    }
}