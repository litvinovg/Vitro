/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.policy.setup;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.ActiveIdentifierBundleFactories;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundleFactory;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.factory.HasPermissionSetFactory;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.factory.HasProfileFactory;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.factory.HasProxyEditingRightsFactory;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.factory.IsRootUserFactory;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.factory.IsUserFactory;
import edu.cornell.mannlib.vitro.webapp.auth.policy.PolicyLoader;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelAccess;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelAccess.WhichService;
import edu.cornell.mannlib.vitro.webapp.modelaccess.ModelNames;
import edu.cornell.mannlib.vitro.webapp.rdfservice.RDFServiceException;
import edu.cornell.mannlib.vitro.webapp.rdfservice.adapters.GraphUtils;
import edu.cornell.mannlib.vitro.webapp.startup.StartupStatus;
import org.apache.jena.graph.Graph;
import org.apache.jena.ontology.OntModel;

/**
 * Set up the common policy family, with Identifier factories.
 */
public class CommonPolicyFamilySetup implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext ctx = sce.getServletContext();
		StartupStatus ss = StartupStatus.getBean(ctx);

		try {
		    PolicyLoader.initialize(ModelAccess.getInstance().getRDFService(WhichService.CONFIGURATION));
		    PolicyLoader.getInstance().loadPolicies();
			factory(new IsUserFactory());
			factory(new IsRootUserFactory());
			factory(new HasProfileFactory());
			factory(new HasPermissionSetFactory());
			factory(new HasProxyEditingRightsFactory());
			registerDefaultPermissionsListener();
		} catch (Exception e) {
			ss.fatal(this, "could not run CommonPolicyFamilySetup", e);
		}
	}

	private void registerDefaultPermissionsListener() throws RDFServiceException {
	    OntModel tbox = ModelAccess.getInstance().getOntModel(ModelNames.TBOX_ASSERTIONS);
        Graph unwrappedGraph = GraphUtils.unwrapUnionGraphs(tbox.getGraph());
        unwrappedGraph.getEventManager().register(new DefaultPermissionListener());
    }

    private void factory(IdentifierBundleFactory factory) {
		ActiveIdentifierBundleFactories.addFactory(factory);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) { /* nothing */
	}

}
