/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.ActionRequest;
import edu.cornell.mannlib.vitro.webapp.beans.DataProperty;

/** Should we allow the user to see this DataProperty? */
public class DisplayDataProperty extends ActionRequest {
	private final DataProperty dataProperty;

	public DisplayDataProperty(DataProperty dataProperty) {
		this.dataProperty = dataProperty;
	}

	public DataProperty getDataProperty() {
		return dataProperty;
	}

	@Override
	public String toString() {
		return "DisplayDataProperty[" + dataProperty + "]";
	}
}
