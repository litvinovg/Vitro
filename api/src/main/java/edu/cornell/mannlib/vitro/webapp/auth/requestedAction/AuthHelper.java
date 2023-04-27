package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import java.util.Arrays;
import java.util.List;

public class AuthHelper {

    static class OrAuthorizationRequest extends ActionRequest {
    	private final ActionRequest ar1;
    	private final ActionRequest ar2;
    
    	private OrAuthorizationRequest(ActionRequest ar1, ActionRequest ar2) {
    		this.ar1 = ar1;
    		this.ar2 = ar2;
    	}
    	@Override
        public boolean isContainer() {
            return true;
        }

        public List<ActionRequest> getItems(){
            return Arrays.asList(ar1, ar2);
        };

    
    	@Override
    	public String toString() {
    		return "(" + ar1 + " || " + ar2 + ")";
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

    
    public static final ActionRequest AUTHORIZED = new AllowRequest();
    public static final ActionRequest UNAUTHORIZED = new DenyRequest();

}
