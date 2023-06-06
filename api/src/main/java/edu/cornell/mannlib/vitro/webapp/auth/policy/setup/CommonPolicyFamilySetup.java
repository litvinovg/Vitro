/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.policy.setup;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.ActiveIdentifierBundleFactories;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundleFactory;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.factory.HasPermissionFactory;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.factory.HasPermissionSetFactory;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.factory.HasProfileOrIsBlacklistedFactory;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.factory.HasProxyEditingRightsFactory;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.factory.IsRootUserFactory;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.factory.IsUserFactory;
import edu.cornell.mannlib.vitro.webapp.auth.policy.AccessRulesPolicy;
import edu.cornell.mannlib.vitro.webapp.auth.policy.PermissionsPolicy;
import edu.cornell.mannlib.vitro.webapp.auth.policy.PolicyStore;
import edu.cornell.mannlib.vitro.webapp.auth.policy.ifaces.PolicyIface;
import edu.cornell.mannlib.vitro.webapp.startup.StartupStatus;

/**
 * Set up the common policy family, with Identifier factories.
 */
public class CommonPolicyFamilySetup implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext ctx = sce.getServletContext();
		StartupStatus ss = StartupStatus.getBean(ctx);

		try {
			policy(new PermissionsPolicy());

			factory(new IsUserFactory());
			factory(new IsRootUserFactory());
			factory(new HasProfileOrIsBlacklistedFactory());
			factory(new HasPermissionSetFactory());
			factory(new HasPermissionFactory());
			factory(new HasProxyEditingRightsFactory());
		} catch (Exception e) {
			ss.fatal(this, "could not run CommonPolicyFamilySetup", e);
		}
	}

	private void policy(PolicyIface policy) {
		PolicyStore.addPolicy(policy);
	}

	private void factory(IdentifierBundleFactory factory) {
		ActiveIdentifierBundleFactories.addFactory(factory);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) { /* nothing */
	}

}
