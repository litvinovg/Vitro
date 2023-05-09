/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.policy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.objects.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyDecision;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyIface;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;
import edu.cornell.mannlib.vitro.webapp.auth.rules.AccessRule;
import edu.cornell.mannlib.vitro.webapp.auth.rules.AccessRuleStore;
import edu.cornell.mannlib.vitro.webapp.auth.rules.EntityAccessRuleHelper;

/**
 * The user is authorized to perform the RequestedAction if one of his
 * Permissions will authorize it.
 */
public class AccessRulesPolicy implements PolicyIface {
    private static final Log log = LogFactory.getLog(AccessRulesPolicy.class);

    @Override
    public PolicyDecision decide(AuthorizationRequest ar) {
        IdentifierBundle ac_subject = ar.getIds();
        AccessObject whatToAuth = ar.getAccessObject();
        if (ac_subject == null) {
            return defaultDecision("whomToAuth was null");
        }
        if (whatToAuth == null) {
            return defaultDecision("whatToAuth was null");
        }

        for (AccessRule rule : AccessRuleStore.getInstance().getFilteredRules(ar)) {
            if (EntityAccessRuleHelper.isAuthorizedPermission(ar, rule)) {
                log.debug("Access rule " + rule + " approves request " + whatToAuth);
                return new BasicPolicyDecision(DecisionResult.AUTHORIZED, "PermissionsPolicy: approved by " + rule);
            } else {
                log.trace("Access rule " + rule + " denies request " + whatToAuth);
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
