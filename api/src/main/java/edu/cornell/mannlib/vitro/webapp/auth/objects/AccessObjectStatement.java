package edu.cornell.mannlib.vitro.webapp.auth.objects;

import org.apache.jena.ontology.OntModel;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessObjectType;
import edu.cornell.mannlib.vitro.webapp.beans.Property;

public class AccessObjectStatement {

    private OntModel model = null;
    private String subject = null;
    private Property predicate = null;
    private String object = null;

    public OntModel getModel() {
        return model;
    }

    public void setModel(OntModel model) {
        this.model = model;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Property getPredicate() {
        return predicate;
    }

    public void setPredicate(Property predicate) {
        this.predicate = predicate;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }


    public String[] getResourceUris(AccessObjectType type) {
        if (AccessObjectType.DATA_PROPERTY_STMT.equals(type)) {
            return new String[] { getSubject() };
        } else if (AccessObjectType.OBJECT_PROPERTY_STMT.equals(type)) {
            return new String[] { getSubject(), getObject() };
        }
        return new String[0];
    }
}
