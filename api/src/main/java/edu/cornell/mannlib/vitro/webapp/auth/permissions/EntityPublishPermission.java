/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject;

import java.util.List;

public class EntityPublishPermission extends EntityPermission {

    public EntityPublishPermission(String uri) {
        super(uri);
    }

    @Override
    public boolean isAuthorized(List<String> personUris, AccessObject whatToAuth) {
        return EntityPermissionHelper.isAuthorizedEntityPublishPermission(personUris, whatToAuth, this);
    }
}
