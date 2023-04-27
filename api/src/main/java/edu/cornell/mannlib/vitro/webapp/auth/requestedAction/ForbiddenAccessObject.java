package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;

public class ForbiddenAccessObject extends AccessObject {

    public ForbiddenAccessObject() {
        predefinedDecision = DecisionResult.UNAUTHORIZED;
    }
}
