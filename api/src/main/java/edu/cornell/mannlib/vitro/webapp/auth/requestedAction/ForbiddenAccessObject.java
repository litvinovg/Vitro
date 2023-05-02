package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;

public class ForbiddenAccessObject implements AuthorizationRequest {

    private DecisionResult predefinedDecision;

    public ForbiddenAccessObject() {
        predefinedDecision = DecisionResult.UNAUTHORIZED;
    }

    @Override
    public DecisionResult getPredefinedDecision() {
        return predefinedDecision;
    }

    @Override
    public AccessObject getAccessObject() {
        return null;
    }

    @Override
    public AccessOperation getAccessOperation() {
        return null;
    }
}
