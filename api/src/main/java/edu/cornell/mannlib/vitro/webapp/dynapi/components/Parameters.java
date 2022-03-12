package edu.cornell.mannlib.vitro.webapp.dynapi.components;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Parameters implements Removable {

	Map <String, Parameter> params;
	
	public Parameters() {
		params = new HashMap<String,Parameter>();
	}
	
	public Parameters(Map<String,Parameter> params) {
		params = new HashMap<String,Parameter>(params);
	}
	
	public void add(Parameter param) {
		params.put(param.getName(), param);
	}
	
	public Set<String> getNames() {
		return params.keySet();
	}
	
	public Parameter get(String name) {
		return params.get(name);
	}
	
	@Override
	public void dereference() {
		for (String name : params.keySet()) {
			params.get(name).dereference();
		}
		params = null;
	}
	
	public Parameters shallowCopy() {
		return new Parameters(params);
	}

	public void remove(Parameters paramsToRemove) {
		Set<String> names = paramsToRemove.getNames();
		for (String name : names) {
			if (params.containsKey(name)){
				Parameter param = params.get(name);
				if (param.equals(paramsToRemove.get(name))) {
					params.remove(name);
				}
			}
		}
	}

	public void add(Parameters paramsToAdd) {
		Set<String> names = paramsToAdd.getNames();
		for (String name : names) {
			if (params.containsKey(name)){
				Parameter param = params.get(name);
				if (!param.equals(paramsToAdd.get(name))) {
					add(paramsToAdd.get(name));
				}
			}
		}		
	}
}
