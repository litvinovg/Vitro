package edu.cornell.mannlib.vitro.webapp.auth.requestedAction;

import java.util.Arrays;
import java.util.List;

public class AuthHelper {

    static class OrAuthorizationRequest extends AccessObject {
    	private final AccessObject ar1;
    	private final AccessObject ar2;
    
    	private OrAuthorizationRequest(AccessObject ar1, AccessObject ar2) {
    		this.ar1 = ar1;
    		this.ar2 = ar2;
    	}
    	@Override
        public boolean isContainer() {
            return true;
        }

        public List<AccessObject> getItems(){
            return Arrays.asList(ar1, ar2);
        };

    
    	@Override
    	public String toString() {
    		return "(" + ar1 + " || " + ar2 + ")";
    	}
    
    }

    public static AccessObject logicOr(AccessObject fist, AccessObject second) {
        if (fist == null) {
            return second;
        } else if (second == null) {
            return fist;
        } else {
            return new AuthHelper.OrAuthorizationRequest(fist, second);
        }
    }

    
    public static final AccessObject AUTHORIZED = new AllowedAccessObject();
    public static final AccessObject UNAUTHORIZED = new ForbiddenAccessObject();

}
