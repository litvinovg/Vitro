/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.policy;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.DecisionResult;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyDecision;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyIface;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessOperation;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt.ObjectPropertyStatementAccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt.DropObjectPropertyStatement;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt.EditObjectPropertyStatement;
import edu.cornell.mannlib.vitro.webapp.dao.DisplayVocabulary;

/**
 * Don't allow user to edit or drop the HomeMenuItem statement.
 */
public class RestrictHomeMenuItemEditingPolicy implements PolicyIface {

	@Override
	public PolicyDecision decide(IdentifierBundle whoToAuth,
			AccessObject whatToAuth, AccessOperation operation) {
		if (whatToAuth instanceof EditObjectPropertyStatement) {
			return isAuthorized((EditObjectPropertyStatement) whatToAuth);
		} else if (whatToAuth instanceof DropObjectPropertyStatement) {
			return isAuthorized((DropObjectPropertyStatement) whatToAuth);
		} else {
			return notHandled();
		}
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
