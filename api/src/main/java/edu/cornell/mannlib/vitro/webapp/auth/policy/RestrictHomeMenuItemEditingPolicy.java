/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.policy;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.cornell.mannlib.vitro.webapp.auth.attributes.AccessOperation;
import edu.cornell.mannlib.vitro.webapp.auth.objects.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.objects.ObjectPropertyStatementAccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyDecision;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyIface;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AuthorizationRequest;
import edu.cornell.mannlib.vitro.webapp.dao.DisplayVocabulary;

/**
 * Don't allow user to edit or drop the HomeMenuItem statement.
 */
public class RestrictHomeMenuItemEditingPolicy implements PolicyIface {

	@Override
	public PolicyDecision decide(AuthorizationRequest ar) {
        AccessObject whatToAuth = ar.getAccessObject();
        AccessOperation operation = ar.getAccessOperation();
	    if(AccessOperation.EDIT.equals(operation) || AccessOperation.DROP.equals(operation)) {
	        if (whatToAuth instanceof ObjectPropertyStatementAccessObject) {
	            return isAuthorized((ObjectPropertyStatementAccessObject) whatToAuth);
	        }
	    }
		return notHandled();
	}

	private PolicyDecision isAuthorized(
			ObjectPropertyStatementAccessObject whatToAuth) {
		if (whatToAuth.getPredicateUri()
				.equals(DisplayVocabulary.HAS_ELEMENT)
				&& whatToAuth.getObjectUri().equals(
						DisplayVocabulary.HOME_MENU_ITEM)) {
			return notAuthorized();
		} else {
			return notHandled();
		}
	}

	private BasicPolicyDecision notHandled() {
		return new BasicPolicyDecision(DecisionResult.INCONCLUSIVE,
				"Doesn't handle this type of request");
	}

	private BasicPolicyDecision notAuthorized() {
		return new BasicPolicyDecision(DecisionResult.UNAUTHORIZED,
				"Can't edit home menu item.");
	}

	public static class Setup implements ServletContextListener {
		@Override
		public void contextInitialized(ServletContextEvent sce) {
			PolicyStore.addPolicyAtFront(new RestrictHomeMenuItemEditingPolicy());
		}

		@Override
		public void contextDestroyed(ServletContextEvent ctx) {
			// Nothing to do here.
		}

	}
}
