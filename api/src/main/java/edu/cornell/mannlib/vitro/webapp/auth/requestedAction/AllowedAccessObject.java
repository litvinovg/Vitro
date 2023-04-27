package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;

public class AllowedAccessObject extends AccessObject {

    public AllowedAccessObject() {
        predefinedDecision = DecisionResult.AUTHORIZED;
    }
}
