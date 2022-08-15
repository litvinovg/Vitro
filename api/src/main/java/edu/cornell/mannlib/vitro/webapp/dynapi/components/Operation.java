package edu.cornell.mannlib.vitro.webapp.dynapi.components;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.dynapi.data.DataStore;
import edu.cornell.mannlib.vitro.webapp.dynapi.data.Data;

public abstract class Operation implements RunnableComponent, ParameterInfo {

    private static final Log log = LogFactory.getLog(Operation.class);

    public boolean isInputValid(DataStore dataStore) {
        Parameters inputParams = getInputParams();
        for (String name : inputParams.getNames()) {
            if (!dataStore.contains(name)) {
                log.error("Parameter " + name + " not found");
                return false;
            }
            Parameter param = inputParams.get(name);
            Data data = dataStore.getData(name);
            if (!param.isValid(name, data)) {
                return false;
            }
        }
        return true;
    }

    public boolean isOutputValid(DataStore dataStore) {
        Parameters providedParams = getOutputParams();
        if (providedParams != null) {
            for (String name : providedParams.getNames()) {
                if (!dataStore.contains(name)) {
                    log.error("Parameter " + name + " not found");
                    return false;
                }
            }
        }

        return true;
    }

}