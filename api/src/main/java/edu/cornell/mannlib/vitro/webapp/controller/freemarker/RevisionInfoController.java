/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.controller.freemarker;

import java.util.HashMap;
import java.util.Map;

import edu.cornell.mannlib.vitro.webapp.auth.permissions.SimplePermissions;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.ActionRequest;
import edu.cornell.mannlib.vitro.webapp.config.RevisionInfoBean;
import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.responsevalues.ResponseValues;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.responsevalues.TemplateResponseValues;

import javax.servlet.annotation.WebServlet;

/**
 * Display the detailed revision information.
 */
@WebServlet(name = "RevisionInfoController", urlPatterns = {"/revisionInfo"} )
public class RevisionInfoController extends FreemarkerHttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String TEMPLATE_DEFAULT = "revisionInfo.ftl";

    public static final ActionRequest REQUIRED_ACTIONS = SimplePermissions.SEE_REVISION_INFO.actionRequest;

    @Override
    protected ActionRequest requiredActions(VitroRequest vreq) {
    	return REQUIRED_ACTIONS;
    }

    @Override
    protected ResponseValues processRequest(VitroRequest vreq) {
        Map<String, Object> body = new HashMap<String, Object>();

        body.put("revisionInfoBean", RevisionInfoBean.getBean(getServletContext()));

        return new TemplateResponseValues(TEMPLATE_DEFAULT, body);
    }

    @Override
    protected String getTitle(String siteName, VitroRequest vreq) {
    	return "Revision Information for " + siteName;
    }

}
