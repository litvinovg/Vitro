/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.startup.StartupStatus;

/**
 * A collection of Permission objects, keyed by URI. Resides in the
 * ServletContext.
 *
 * This is not thread-safe, so Permissions should be added only during context
 * initialization.
 */
public class PermissionRegistry {
	private static final Log log = LogFactory.getLog(PermissionRegistry.class);

	// ----------------------------------------------------------------------
	// The factory
	// ----------------------------------------------------------------------

	private static PermissionRegistry INSTANCE;

	public static void setInstance(PermissionRegistry pr) {
        INSTANCE = pr;
    }

    /**
	 * Has the registry been created yet?
	 */
	public static boolean isRegistryCreated(ServletContext ctx) {
		return INSTANCE != null;
	}

	/**
	 * Create the registry and store it in the context.
	 */
	public static void createRegistry(ServletContext ctx,
			Collection<? extends AccessRule> permissions) {
		if (ctx == null) {
			throw new NullPointerException("ctx may not be null.");
		}
		if (permissions == null) {
			throw new NullPointerException("permissions may not be null.");
		}
		if (INSTANCE != null) {
			throw new IllegalStateException(
					"PermissionRegistry has already been set.");
		}

		PermissionRegistry registry = new PermissionRegistry();
		registry.addPermissions(permissions);
		INSTANCE = registry;
	}

	/**
	 * Get the registry from the context. If there isn't one, throw an
	 * exception.
	 */
	@Deprecated
	public static PermissionRegistry getRegistry(ServletContext ctx) {
		if (INSTANCE == null) {
			throw new IllegalStateException(
					"PermissionRegistry has not been set.");
		} 

		return INSTANCE;
	}

    public static PermissionRegistry getRegistry() {
        if (INSTANCE == null) {
            throw new IllegalStateException("PermissionRegistry has not been set.");
        }

        return INSTANCE;
    }

	// ----------------------------------------------------------------------
	// The instance
	// ----------------------------------------------------------------------

	private final Map<String, AccessRule> map = new HashMap<>();

	/**
	 * This class is not thread-safe, so permissions should be added only during
	 * context initialization.
	 */
	public void addPermissions(Collection<? extends AccessRule> permissions) {
		for (AccessRule p : permissions) {
			addPermission(p);
		}
	}

	/**
	 * This class is not thread-safe, so permissions should be added only during
	 * context initialization.
	 */
	public void addPermission(AccessRule p) {
		String uri = p.getUri();
		if (map.containsKey(uri)) {
			throw new IllegalStateException("A Permission is already "
					+ "registered with this URI: '" + uri + "'.");
		}
		map.put(uri, p);
	}

	/**
	 * Is there a Permission registered with this URI?
	 */
	public boolean isPermission(String uri) {
		return map.containsKey(uri);
	}

	/**
	 * Get the permission that is registered with this URI. If there is no such
	 * Permission, return a BrokenPermission that always denies authorization.
	 *
	 * If you want to know whether an actual Permission has been registered at
	 * this URI, call isPermission() instead.
	 */
	public AccessRule getPermission(String uri) {
		AccessRule p = map.get(uri);
		if (p == null) {
			log.warn("No Permission is registered for '" + uri + "'");
			return new BrokenPermission(uri);
		}

		return p;
	}

	// ----------------------------------------------------------------------
	// Setup class
	// ----------------------------------------------------------------------

	public static class Setup implements ServletContextListener {
		@Override
		public void contextInitialized(ServletContextEvent sce) {
			ServletContext ctx = sce.getServletContext();
			StartupStatus ss = StartupStatus.getBean(ctx);
		    //Value is not used. Triggers static fields initialization
			SimplePermission test = SimplePermission.EDIT_OWN_ACCOUNT;

			try {
				List<AccessRule> permissions = new ArrayList<AccessRule>();

				permissions.addAll(SimpleAccessRules.getAllInstances());
				permissions.addAll(EntityAccessRules.getAllInstances());

				PermissionRegistry.createRegistry(ctx, permissions);

				ss.info(this, "Created the PermissionRegistry with "
						+ permissions.size() + " permissions.");
			} catch (Exception e) {
				ss.fatal(this, "Failed to initialize the PermissionRegistry.",
						e);
			}
		}

		@Override
		public void contextDestroyed(ServletContextEvent sce) {
		}
	}
}
