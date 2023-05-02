/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.EntityUriObjectAttribute;
import edu.cornell.mannlib.vitro.webapp.dao.PropertyDao;

import java.util.*;

/**
 * A permission that is to be applied to "entities"
 * An entity may be a class, property or faux property defined in the ontologies.
 * Subclass to define the type of permission that is being granted (e.g. display, update, publish)
 */
public class EntityAccessRule extends AccessRule {

    public void setUri(String uri) {
        this.uri = uri;
        addAttribute(new EntityUriObjectAttribute(uri));
    }

    private String uri;

    public String getUri() {
        return uri;
    }
    
    /**
     * Instance fields for each EntityPermission
     */
    private final Set<PropertyDao.FullPropertyKey> authorizedKeys = Collections.synchronizedSet(new HashSet<>());
    
    public Set<PropertyDao.FullPropertyKey> getAuthorizedKeys() {
        return authorizedKeys;
    }

    private final Set<String> authorizedResources = Collections.synchronizedSet(new HashSet<>());
    
    public Set<String> getAuthorizedResources() {
        return authorizedResources;
    }

    boolean limitToRelatedUser = false;

    public boolean isLimitToRelatedUser() {
        return limitToRelatedUser;
    }

}
