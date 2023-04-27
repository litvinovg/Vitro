package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;

public class AllowRequest extends ActionRequest {

    public AllowRequest() {
        predefinedDecision = DecisionResult.AUTHORIZED;
    }
}
