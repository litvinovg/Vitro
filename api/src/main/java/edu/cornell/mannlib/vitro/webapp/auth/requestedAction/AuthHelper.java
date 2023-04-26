package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyIface;

public class AuthHelper {

    static class OrAuthorizationRequest extends AuthorizationRequest {
    	private final AuthorizationRequest ar1;
    	private final AuthorizationRequest ar2;
    
    	private OrAuthorizationRequest(AuthorizationRequest ar1,
    			AuthorizationRequest ar2) {
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

    private static class AndAuthorizationRequest extends AuthorizationRequest {
    	private final AuthorizationRequest ar1;
    	private final AuthorizationRequest ar2;
    
    	private AndAuthorizationRequest(AuthorizationRequest ar1,
    			AuthorizationRequest ar2) {
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

    public static AuthorizationRequest logicOr(AuthorizationRequest fist, AuthorizationRequest second) {
        if (fist == null) {
            return second;
        } else if (second == null) {
            return fist;
        } else {
            return new AuthHelper.OrAuthorizationRequest(fist, second);
        }
    }

    public static final AuthorizationRequest AUTHORIZED = new AuthorizationRequest() {
    	@Override
    	public boolean isAuthorized(IdentifierBundle ids, PolicyIface policy) {
    		return true;
    	}
    };
    public static final AuthorizationRequest UNAUTHORIZED = new AuthorizationRequest() {
    	@Override
    	public boolean isAuthorized(IdentifierBundle ids, PolicyIface policy) {
    		return false;
    	}
    };

}
