package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;

public class AllowedAccessObject implements AuthorizationRequest{

    private DecisionResult decision;

    public AllowedAccessObject() {
        decision = DecisionResult.AUTHORIZED;
    }

    @Override
    public DecisionResult getPredefinedDecision() {
        return decision;
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
