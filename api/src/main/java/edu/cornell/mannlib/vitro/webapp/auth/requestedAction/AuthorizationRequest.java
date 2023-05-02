package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import java.util.Collections;
import java.util.List;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;

public interface AuthorizationRequest {

    public default boolean isContainer() {
        return false;
    }

    public default DecisionResult getPredefinedDecision(){
        return DecisionResult.INCONCLUSIVE;
    }
    
    public default List<AuthorizationRequest> getItems() {
        return Collections.emptyList();
    }
    
    public AccessObject getAccessObject();
    
    public AccessOperation getAccessOperation();
    
}