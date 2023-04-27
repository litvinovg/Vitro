/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.requestedAction.publish;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display.ObjectPropertyAccessObject;
import edu.cornell.mannlib.vitro.webapp.beans.ObjectProperty;

/** Should we allow the user to publish this ObjectProperty in Linked Open Data? */
public class PublishObjectProperty extends ObjectPropertyAccessObject {
    public PublishObjectProperty(ObjectProperty objectProperty) {
        super(objectProperty);
    }
}
