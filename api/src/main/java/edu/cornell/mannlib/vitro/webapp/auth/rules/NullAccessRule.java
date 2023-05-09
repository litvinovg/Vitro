/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.rules;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;

/**
 * This is what the PermissionRegistry hands out if you ask for a Permission
 * that it doesn't know about. Nothing is authorized by this Permission.
 */
public class NullAccessRule extends AccessRule {

    @Override
    public String getObjectUri() {
        return "";
    }

    public boolean match(AuthorizationRequest ar) {
        return false;
    }
    @Override
    public void setObjectUri(String uri) {
    }
    
}
