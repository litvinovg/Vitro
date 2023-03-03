package edu.cornell.mannlib.vitro.webapp.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PersonResourceMapCache implements AutoCloseable {
	private static final Log log = LogFactory.getLog(PersonResourceMapCache.class);

    private static ThreadLocal<HashMap<String, List<String>>> threadLocal = new ThreadLocal<HashMap<String, List<String>>>();

    public PersonResourceMapCache() {
		threadLocal.set(new HashMap<String, List<String>>());
		log.debug("Person resource map cache initialized");
	}
    
    @Override
	public void close() throws IOException {
		threadLocal.remove();
		log.debug("Person resource map cache closed");
	}

	static HashMap<String, List<String>> get() {
		HashMap<String, List<String>> personResourceMap = threadLocal.get();
    	if (personResourceMap == null) {
    		personResourceMap = new HashMap<String, List<String>>();
    		log.debug("Use a non-cached person resource map");
    	} else {
    		log.debug("Use cached person resource map");
    	}
		return personResourceMap;
	}

	static void update(HashMap<String, List<String>> personResourceMap) {
		if (threadLocal.get() != null ) {
			threadLocal.set(personResourceMap);
    		log.debug("Person resource map cache has been updated");
		} else {
			log.debug("Person resource map cache has not been updated as it wasn't initialized");	
		}
	}
}
