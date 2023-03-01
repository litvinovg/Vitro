/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.auth.requestedAction.propstmt;

import org.apache.jena.ontology.OntModel;

import edu.cornell.mannlib.vitro.webapp.beans.DataPropertyStatement;

import javax.servlet.http.HttpServletRequest;

/**
 * Should we allow the user to edit this DataPropertyStatement in this model?
 */
public class EditDataPropertyStatement extends
		AbstractDataPropertyStatementAction {
	public EditDataPropertyStatement(HttpServletRequest request, OntModel ontModel, String subjectUri,
			String predicateUri, String dataValue) {
		super(request, ontModel, subjectUri, predicateUri, dataValue);
	}

	public EditDataPropertyStatement(HttpServletRequest request, OntModel ontModel,
									 DataPropertyStatement dps) {
		super(request, ontModel, dps);
	}
}
