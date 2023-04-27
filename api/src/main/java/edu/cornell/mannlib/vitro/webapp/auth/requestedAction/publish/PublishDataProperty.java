/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.requestedAction.publish;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display.DataPropertyAccessObject;
import edu.cornell.mannlib.vitro.webapp.beans.DataProperty;

/** Should we allow the user to publish this DataProperty in Linked Open Data? */
public class PublishDataProperty extends DataPropertyAccessObject {
    public PublishDataProperty(DataProperty dataProperty) {
        super(dataProperty);
    }
}
