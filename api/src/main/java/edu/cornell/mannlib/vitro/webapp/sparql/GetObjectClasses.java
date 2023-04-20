/* $This file is distributed under the terms of the license in LICENSE$ */
package edu.cornell.mannlib.vitro.webapp.sparql;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.mannlib.vedit.controller.BaseEditController;
import edu.cornell.mannlib.vitro.webapp.auth.permissions.SimplePermissions;
import edu.cornell.mannlib.vitro.webapp.beans.ObjectProperty;
import edu.cornell.mannlib.vitro.webapp.beans.VClass;
import edu.cornell.mannlib.vitro.webapp.beans.VClassGroup;
import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.dao.ObjectPropertyDao;
import edu.cornell.mannlib.vitro.webapp.dao.VClassDao;

/**
 * This servlet gets all the range classes for a given predicate.
 */

@WebServlet(name = "GetObjectClasses", urlPatterns = {"/admin/getObjectClasses"})
public class GetObjectClasses extends BaseEditController {

	private static final Log log = LogFactory.getLog(GetObjectClasses.class);

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 *
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAuthorizedToDisplayPage(request, response,
				SimplePermissions.USE_MISCELLANEOUS_PAGES.actionRequest)) {
        	return;
		}

		VitroRequest vreq = new VitroRequest(request);

		String predicate = vreq.getParameter("predicate");
		if (predicate == null || predicate.trim().equals("")) {
			return;
		}

		ObjectPropertyDao odao = vreq.getUnfilteredWebappDaoFactory()
				.getObjectPropertyDao();
		ObjectProperty oprop = (ObjectProperty) odao
				.getObjectPropertyByURI(predicate);

		VClassDao vcDao = vreq.getUnfilteredWebappDaoFactory().getVClassDao();
		VClass vClass = (oprop.getRangeVClassURI() != null) ? vcDao
				.getVClassByURI(oprop.getRangeVClassURI()) : null;

		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		StringBuilder respo = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		respo.append("<options>");
		if (vClass != null) {
			respo.append("<option>" + "<key>").append(vClass.getPickListName()).append("</key>").append("<value>").append(vClass.getURI()).append("</value>").append("</option>");
		} else {
			List<VClassGroup> classGroups = vreq.getUnfilteredWebappDaoFactory()
					.getVClassGroupDao().getPublicGroupsWithVClasses(true,
							true, false); // order by displayRank, include
											// uninstantiated classes, don't get
											// the counts of individuals

			for (VClassGroup group : classGroups) {
				List<VClass> classes = group.getVitroClassList();
				for (VClass clazz : classes) {
					respo.append("<option>" + "<key>").append(clazz.getPickListName()).append("</key>").append("<value>").append(clazz.getURI()).append("</value>").append("</option>");
				}
			}
		}

		/*
		 * VClass clazz = oprop.getRangeVClass();
		 * response.setContentType("text/xml");
		 * response.setCharacterEncoding("UTF-8"); PrintWriter out =
		 * response.getWriter(); String respo =
		 * "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; respo += "<options>";
		 *
		 * if (clazz != null) { respo += "<option>" + "<key>" +
		 * clazz.getPickListName() + "</key>" + "<value>" + clazz.getURI() +
		 * "</value>" + "</option>"; } else{ List classGroups =
		 * vreq.getFullWebappDaoFactory
		 * ().getVClassGroupDao().getPublicGroupsWithVClasses(true,true,false);
		 * // order by displayRank, include uninstantiated classes, don't get
		 * the counts of individuals Iterator classGroupIt =
		 * classGroups.iterator(); while (classGroupIt.hasNext()) { VClassGroup
		 * group = (VClassGroup)classGroupIt.next(); List classes =
		 * group.getVitroClassList(); Iterator classIt = classes.iterator();
		 * while (classIt.hasNext()) { clazz = (VClass) classIt.next();
		 * System.out.println(clazz.getPickListName()); respo += "<option>" +
		 * "<key>" + clazz.getPickListName() + "</key>" + "<value>" +
		 * clazz.getURI() + "</value>" + "</option>"; } }
		 *
		 * }
		 */
		respo.append("</options>");
		out.println(respo);
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to
	 * post.
	 *
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
