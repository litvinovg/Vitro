package edu.cornell.mannlib.vitro.webapp.dynapi.data.types;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;

import edu.cornell.mannlib.vitro.webapp.dynapi.components.Removable;
import edu.cornell.mannlib.vitro.webapp.utils.configuration.Property;

public class RDFType implements Removable {
    protected String name;

    public RDFDatatype getRDFDataType() {
        return new XSDDatatype(name);
    }

    @Property(uri = "https://vivoweb.org/ontology/vitro-dynamic-api#name", minOccurs = 1, maxOccurs = 1)
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void dereference() {
        // TODO Auto-generated method stub
    }
    
}
