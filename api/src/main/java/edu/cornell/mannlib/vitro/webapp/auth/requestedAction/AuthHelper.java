package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyIface;

public class AuthHelper {

    static class OrAuthorizationRequest extends ActionRequest {
    	private final ActionRequest ar1;
    	private final ActionRequest ar2;
    
    	private OrAuthorizationRequest(ActionRequest ar1,
    			ActionRequest ar2) {
    		this.ar1 = ar1;
    		this.ar2 = ar2;
    	}
    
    	@Override
    	public boolean isAuthorized(IdentifierBundle ids, PolicyIface policy) {
    		return ar1.isAuthorized(ids, policy)
    				|| ar2.isAuthorized(ids, policy);
    	}
    
    	@Override
    	public String toString() {
    		return "(" + ar1 + " || " + ar2 + ")";
    	}
    
    }

    private static class AndAuthorizationRequest extends ActionRequest {
    	private final ActionRequest ar1;
    	private final ActionRequest ar2;
    
    	private AndAuthorizationRequest(ActionRequest ar1,
    			ActionRequest ar2) {
    		this.ar1 = ar1;
    		this.ar2 = ar2;
    	}
    
    	@Override
    	public boolean isAuthorized(IdentifierBundle ids, PolicyIface policy) {
    		return ar1.isAuthorized(ids, policy)
    				&& ar2.isAuthorized(ids, policy);
    	}
    
    	@Override
    	public String toString() {
    		return "(" + ar1 + " && " + ar2 + ")";
    	}
    
    }

    public static ActionRequest logicOr(ActionRequest fist, ActionRequest second) {
        if (fist == null) {
            return second;
        } else if (second == null) {
            return fist;
        } else {
            return new AuthHelper.OrAuthorizationRequest(fist, second);
        }
    }

    public static final ActionRequest AUTHORIZED = new ActionRequest() {
    	@Override
    	public boolean isAuthorized(IdentifierBundle ids, PolicyIface policy) {
    		return true;
    	}
    };
    public static final ActionRequest UNAUTHORIZED = new ActionRequest() {
    	@Override
    	public boolean isAuthorized(IdentifierBundle ids, PolicyIface policy) {
    		return false;
    	}
    };

}
