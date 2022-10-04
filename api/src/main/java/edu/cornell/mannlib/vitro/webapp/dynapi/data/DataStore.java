package edu.cornell.mannlib.vitro.webapp.dynapi.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.entity.ContentType;

import edu.cornell.mannlib.vitro.webapp.dynapi.data.conversion.LangTag;

public class DataStore {

	protected Map<String, Data> dataMap = new HashMap<>();
	private ContentType responseType = ContentType.APPLICATION_JSON;
	private List<String> acceptLangs = new LinkedList<>();
	private String resourceId = "";


	public DataStore() {
	}

	public void addData(String name, Data data) {
        dataMap.put(name, data);
    }
	
	public Data getData(String name) {
		return dataMap.get(name);
	}
	
	public String getResourceId() {
		return resourceId;
	}
	
	public void setResourceID(String resourceId) {
		this.resourceId  = resourceId;
	}

	public ContentType getResponseType() {
		return responseType;	
	}
	
	public void setResponseType(ContentType contentType) {
		this.responseType = contentType;	
	}

	public void setAcceptLangs(List<String> acceptLangs) {
		this.acceptLangs.addAll(acceptLangs);
	}
	
	public List<String> getAcceptLangs() {
	    return acceptLangs;
	}

	public boolean contains(String name) {
		return dataMap.containsKey(name);
	}

	protected Set<Entry<String, Data>> entrySet() {
		return dataMap.entrySet();
	}
	
	protected Set<String> keySet() {
		return dataMap.keySet();
	}
}
