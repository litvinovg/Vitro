/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.dao.filtering.filters;

import static edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject.SOME_URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vitro.webapp.auth.identifier.ActiveIdentifierBundleFactories;
import edu.cornell.mannlib.vitro.webapp.auth.identifier.IdentifierBundle;
import edu.cornell.mannlib.vitro.webapp.auth.policy.PolicyHelper;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessObject;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessOperation;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display.DisplayDataProperty;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display.DisplayDataPropertyStatement;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display.DisplayObjectProperty;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.display.DisplayObjectPropertyStatement;
import edu.cornell.mannlib.vitro.webapp.beans.DataProperty;
import edu.cornell.mannlib.vitro.webapp.beans.DataPropertyStatement;
import edu.cornell.mannlib.vitro.webapp.beans.ObjectProperty;
import edu.cornell.mannlib.vitro.webapp.beans.ObjectPropertyStatement;
import net.sf.jga.fn.UnaryFunctor;

/**
 * Filter the properties depending on what DisplayByRolePermission is on the
 * request. If no request, or no permission, use the Public permission.
 */
public class FilterByDisplayPermission extends VitroFiltersImpl {
	private static final Log log = LogFactory.getLog(FilterByDisplayPermission.class);
	IdentifierBundle accessSubject = ActiveIdentifierBundleFactories.getUserIdentifierBundle(null);
	AccessOperation requestedOperation = AccessOperation.DISPLAY;

	public FilterByDisplayPermission() {
	    setDataPropertyFilter(new DataPropertyFilterByPolicy());
	    setObjectPropertyFilter(new ObjectPropertyFilterByPolicy());
	    setDataPropertyStatementFilter(new DataPropertyStatementFilterByPolicy());
	    setObjectPropertyStatementFilter(new ObjectPropertyStatementFilterByPolicy());
	}

	boolean checkAuthorization(AccessObject accessObject) {
	    
		boolean decision = PolicyHelper.isAuthorizedForActions(accessSubject, accessObject, requestedOperation);
		log.debug("decision is " + decision);
		return decision;
	}

	/**
	 * Private Classes
	 */

	private class DataPropertyFilterByPolicy extends UnaryFunctor<DataProperty, Boolean> {
		@Override
		public Boolean fn(DataProperty dp) {
			return checkAuthorization(new DisplayDataProperty(dp));
		}
	}

	private class ObjectPropertyFilterByPolicy extends UnaryFunctor<ObjectProperty, Boolean> {
		@Override
		public Boolean fn(ObjectProperty op) {
			return checkAuthorization(new DisplayObjectProperty(op));
		}
	}

	private class DataPropertyStatementFilterByPolicy extends UnaryFunctor<DataPropertyStatement, Boolean> {
		@Override
		public Boolean fn(DataPropertyStatement dps) {
			return checkAuthorization(new DisplayDataPropertyStatement(dps));
		}
	}

	private class ObjectPropertyStatementFilterByPolicy extends UnaryFunctor<ObjectPropertyStatement, Boolean> {
		@Override
		public Boolean fn(ObjectPropertyStatement ops) {
			String subjectUri = ops.getSubjectURI();
			ObjectProperty predicate = getOrCreateProperty(ops);
			String objectUri = ops.getObjectURI();
			return checkAuthorization(new DisplayObjectPropertyStatement(subjectUri, predicate, objectUri));
		}

		/**
		 * It would be nice if every ObjectPropertyStatement held a real
		 * ObjectProperty. If it doesn't, we do the next best thing, but it
		 * won't recognize any applicable Faux properties.
		 */
		private ObjectProperty getOrCreateProperty(ObjectPropertyStatement ops) {
			if (ops.getProperty() != null) {
				return ops.getProperty();
			}
			if (ops.getPropertyURI() ==  null) {
				return null;
			}
			ObjectProperty op = new ObjectProperty();
			op.setURI(ops.getPropertyURI());
			op.setDomainVClassURI(SOME_URI);
			op.setRangeVClassURI(SOME_URI);
			return op;
		}
	}
}
