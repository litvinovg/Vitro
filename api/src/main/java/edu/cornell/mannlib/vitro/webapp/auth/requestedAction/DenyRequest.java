package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;

public class DenyRequest extends ActionRequest {

    public DenyRequest() {
        predefinedDecision = DecisionResult.UNAUTHORIZED;
    }
}
