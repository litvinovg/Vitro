/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.rules;

import java.util.*;

@Deprecated
public class EntityAccessRule extends AccessRule {


    private final Set<String> authorizedResources = Collections.synchronizedSet(new HashSet<>());
    
    public Set<String> getAuthorizedResources() {
        return authorizedResources;
    }

    boolean limitToRelatedUser = false;

    public boolean isLimitToRelatedUser() {
        return limitToRelatedUser;
    }

}
