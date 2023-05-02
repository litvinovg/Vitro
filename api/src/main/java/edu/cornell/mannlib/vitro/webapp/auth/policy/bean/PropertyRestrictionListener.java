/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.policy.bean;

import edu.cornell.mannlib.vitro.webapp.auth.permissions.EntityAccessRules;
import edu.cornell.mannlib.vitro.webapp.beans.Property;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vedit.beans.EditProcessObject;
import edu.cornell.mannlib.vedit.listener.ChangeListener;

/**
 * Add this ChangeListener to your EditProcessObject when modifying the
 * ontology, and we will refresh the PropertyRestrictionPolicyHelper bean as
 * appropriate.
 */
public class PropertyRestrictionListener implements ChangeListener {
	private static final Log log = LogFactory.getLog(PropertyRestrictionListener.class);

	/**
	 * If the deleted property had a non-null restriction, rebuild the bean.
	 */
	@Override
	public void doDeleted(Object oldObj, EditProcessObject epo) {
		if (oldObj instanceof Property) {
			EntityAccessRules.updateAllPermissionsFor((Property)oldObj);
		} else {
			log.warn("Not an instance of Property: " + oldObj);
		}
	}

	/**
	 * Update the inserted property.
	 */
	@Override
	public void doInserted(Object newObj, EditProcessObject epo) {
		if (newObj instanceof Property) {
			EntityAccessRules.updateAllPermissionsFor((Property)newObj);
		} else {
			log.warn("Not an instance of Property: " + newObj);
		}
	}

	/**
	 * Update the changed property.
	 */
	@Override
	public void doUpdated(Object oldObj, Object newObj, EditProcessObject epo) {
		if (newObj instanceof Property) {
			EntityAccessRules.updateAllPermissionsFor((Property)oldObj);
		} else {
			log.warn("Not instances of Property: " + oldObj
					+ ", " + newObj);
		}
	}
}
