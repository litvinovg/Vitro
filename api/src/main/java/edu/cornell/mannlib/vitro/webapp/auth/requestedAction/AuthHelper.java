package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import java.util.Arrays;
import java.util.List;

public class AuthHelper {

    static class OrAuthorizationRequest implements AuthorizationRequest {
    	private final AuthorizationRequest ar1;
    	private final AuthorizationRequest ar2;
    
    	private OrAuthorizationRequest(AuthorizationRequest ar1, AuthorizationRequest ar2) {
    		this.ar1 = ar1;
    		this.ar2 = ar2;
    	}
    	@Override
        public boolean isContainer() {
            return true;
        }

        public List<AuthorizationRequest> getItems(){
            return Arrays.asList(ar1, ar2);
        };

    
    	@Override
    	public String toString() {
    		return "(" + ar1 + " || " + ar2 + ")";
    	}
        @Override
        public AccessObject getAccessObject() {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public AccessOperation getAccessOperation() {
            // TODO Auto-generated method stub
            return null;
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

    
    public static final AuthorizationRequest AUTHORIZED = new AllowedAuthorizationRequest();
    public static final AuthorizationRequest UNAUTHORIZED = new ForbiddenAuthorizationRequest();

}
