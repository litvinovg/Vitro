/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.objects;

import org.apache.jena.ontology.OntModel;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessObjectType;
import edu.cornell.mannlib.vitro.webapp.beans.Property;

/**
 * A base class for requested access objects that involve adding, editing, or deleting
 * object property statements from a model.
 */
public class ObjectPropertyStatementAccessObject extends AccessObject {

	public ObjectPropertyStatementAccessObject(OntModel ontModel, String subjectUri, Property predicate, String objectUri) {
	    setStatementOntModel(ontModel);
        setStatementSubject(subjectUri);
        setStatementPredicate(predicate);
        setStatementObject(objectUri);
	}

    @Override
    public AccessObjectType getType() {
        return AccessObjectType.OBJECT_PROPERTY_STMT;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": <" + getStatementSubject() + "> <" + getStatementPredicateUri() + ">";
    }
}