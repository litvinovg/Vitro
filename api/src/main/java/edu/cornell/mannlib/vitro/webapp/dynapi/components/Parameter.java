package edu.cornell.mannlib.vitro.webapp.dynapi.components;

import edu.cornell.mannlib.vitro.webapp.dynapi.components.types.ParameterType;
import org.apache.jena.datatypes.RDFDatatype;

import edu.cornell.mannlib.vitro.webapp.dynapi.components.validators.Validator;
import edu.cornell.mannlib.vitro.webapp.utils.configuration.Property;

public class Parameter implements Removable{

	private String name;
	private String description;
	private Validators validators = new Validators();
	private ParameterType type;

	public String getName() {
		return name;
	}

	public RDFDatatype getRDFDataType() {
		return type.getRDFDataType();
	}

	public ParameterType getType() {
		return type;
	}

	@Property(uri = "https://vivoweb.org/ontology/vitro-dynamic-api#hasType", minOccurs = 1, maxOccurs = 1)
	public void setParamType(ParameterType type) {
		this.type = type;
	}

	@Property(uri = "https://vivoweb.org/ontology/vitro-dynamic-api#name", minOccurs = 1, maxOccurs = 1)
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	@Property(uri = "https://vivoweb.org/ontology/vitro-dynamic-api#description", maxOccurs = 1)
	public void setDescription(String description) {
		this.description = description;
	}

	@Property(uri = "https://vivoweb.org/ontology/vitro-dynamic-api#hasValidator")
	public void addValidator(Validator validator) {
		validators.add(validator);
	}

	public boolean isValid(String name, String[] values) {
		return validators.isAllValid(name, values);
	}
	
	public boolean equals(Parameter param) {
		if(name == null || !name.equals(param.getName())) {
			return false;
		}
		if (!type.equals(param.getType())) {
			return false;
		}
		return true;
	}

	@Override
	public void dereference() {
	}

}
