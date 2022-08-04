package edu.cornell.mannlib.vitro.webapp.dynapi.data.types;

import edu.cornell.mannlib.vitro.webapp.utils.configuration.Property;

public class ImplementationConfig {

	private Class<?> classObject;
	private String methodName;
	private String methodArguments;
	private boolean staticMethod = false;
	
	public Class<?> getClassObject() {
		return classObject;
	}

	@Property(uri = "https://vivoweb.org/ontology/vitro-dynamic-api#className", minOccurs = 1, maxOccurs = 1)
	public void setClassName(String className) throws ClassNotFoundException {
		this.classObject = Class.forName(className);
	}

	public String getMethodName() {
		return methodName;
	}

	@Property(uri = "https://vivoweb.org/ontology/vitro-dynamic-api#methodName", minOccurs = 1, maxOccurs = 1)
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodArguments() {
		return methodArguments;
	}
	
	@Property(uri = "https://vivoweb.org/ontology/vitro-dynamic-api#methodArguments", minOccurs = 1, maxOccurs = 1)
	public void setMethodArguments(String methodArguments) {
		this.methodArguments = methodArguments;
	}

	public boolean isStatic() {
		return staticMethod;
	}
	
	@Property(uri = "https://vivoweb.org/ontology/vitro-dynamic-api#isStaticMethod", minOccurs = 0, maxOccurs = 1)
	public void setStaticMethod(boolean isStaticMethod) {
		this.staticMethod = isStaticMethod;
	}
}