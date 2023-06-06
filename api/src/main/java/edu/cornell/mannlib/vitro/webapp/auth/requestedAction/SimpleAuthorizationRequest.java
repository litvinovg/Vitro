package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;

public class SimpleAuthorizationRequest implements AuthorizationRequest {

    private AccessObject object;
    public AccessObject getObject() {
        return object;
    }

    public AccessOperation getOperation() {
        return operation;
    }

    private AccessOperation operation;
    
    public SimpleAuthorizationRequest(String namedAccessObject, AccessOperation operation) {
        this.object = new NamedAccessObject(namedAccessObject);
        this.operation = operation;
    }
    
    public SimpleAuthorizationRequest(AccessObject object, AccessOperation operation) {
        this.object = object;
        this.operation = operation;
    }
    
    public SimpleAuthorizationRequest(String namedAccessObject) {
        this.object = new NamedAccessObject(namedAccessObject);
        this.operation = AccessOperation.EXECUTE;
    }

    @Override
    public DecisionResult getPredefinedDecision() {
        return DecisionResult.INCONCLUSIVE;
    }

    @Override
    public AccessObject getAccessObject() {
        return object;
    }

    @Override
    public AccessOperation getAccessOperation() {
        return operation;
    }
}
