package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;

public class ForbiddenAuthorizationRequest implements AuthorizationRequest {

    private DecisionResult predefinedDecision;

    public ForbiddenAuthorizationRequest() {
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
