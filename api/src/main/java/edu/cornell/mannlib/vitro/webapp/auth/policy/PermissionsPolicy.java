/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.policy;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.common.HasAssociatedIndividual;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.common.IdentifierPermissionProvider;
import edu.cornell.mannlib.vitro.webapp.auth.permissions.Permission;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyDecision;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyIface;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.ActionRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * The user is authorized to perform the RequestedAction if one of his
 * Permissions will authorize it.
 */
public class PermissionsPolicy implements PolicyIface {
    private static final Log log = LogFactory.getLog(PermissionsPolicy.class);

    @Override
    public PolicyDecision decide(IdentifierBundle ac_subject, ActionRequest whatToAuth) {
        if (ac_subject == null) {
            return defaultDecision("whomToAuth was null");
        }
        if (whatToAuth == null) {
            return defaultDecision("whatToAuth was null");
        }

        List<String> personUris = new ArrayList<String>(HasAssociatedIndividual.getIndividualUris(ac_subject));

        for (Permission p : IdentifierPermissionProvider.getPermissions(ac_subject)) {
            if (p.isAuthorized(personUris, whatToAuth)) {
                log.debug("Permission " + p + " approves request " + whatToAuth);
                return new BasicPolicyDecision(DecisionResult.AUTHORIZED, "PermissionsPolicy: approved by " + p);
            } else {
                log.trace("Permission " + p + " denies request " + whatToAuth);
            }
        }
        log.debug("No permission will approve " + whatToAuth);
        return defaultDecision("no permission will approve " + whatToAuth);
    }

    /** If the user isn't explicitly authorized, return this. */
    private PolicyDecision defaultDecision(String message) {
        return new BasicPolicyDecision(DecisionResult.INCONCLUSIVE, message);
    }

    @Override
    public String toString() {
        return "PermissionsPolicy - " + hashCode();
    }

}
