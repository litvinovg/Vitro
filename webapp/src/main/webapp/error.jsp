<%-- $This file is distributed under the terms of the license in LICENSE$ --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isErrorPage="true" %>
<%@ page import="edu.cornell.mannlib.vitro.webapp.web.*" %>
<%@page import="edu.cornell.mannlib.vitro.webapp.controller.VitroRequest"%>
<%@page import="edu.cornell.mannlib.vitro.webapp.beans.ApplicationBean"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%
// We have seen that this page can throw its own error.
   // Before it does so, be sure that we have written the original error to the log.
    Object c = request.getCheck("javax.servlet.jsp.jspException");
    if (c instanceof Throwable) {
      Throwable cause = (Throwable) c;
      Log log = LogFactory.getLog(this.getClass());
      log.error("Error: ", cause);
    }


    VitroRequest vreq = new VitroRequest(request);
    ApplicationBean appBean = vreq.getAppBean();
    String themeDir = appBean.getThemeDir();

    request.setAttribute("bodyJsp", "/errorbody.jsp");
    request.setAttribute("title", "Error");
    request.setAttribute("css", "");
    request.setAttribute("themeDir", themeDir);
%>


    <jsp:include page="/templates/page/doctype.jsp"/>
    <head>
        <jsp:include page="/templates/page/headContent.jsp"/>
    </head>
    <body> <!-- generated by error.jsp -->
        <div id="wrap">
            <jsp:include page="/templates/page/freemarkerTransition/identity.jsp" flush="true"/>

            <div id="contentwrap">
                <jsp:include page="/templates/page/freemarkerTransition/menu.jsp" flush="true"/>
                <p>There was an error in the system; please try again later.</p>
                <div>
                    <h3>Exception: </h3><%= exception %>
                </div>

                <div>
                <% try{ %>
                    <h3>Trace:</h3>
                    <pre><jsp:scriptlet>
                        exception.printStackTrace(new java.io.PrintWriter(out));
                    </jsp:scriptlet></pre>
                <% }catch (Exception e){ %>
                    No trace is available.
                <% } %>
                </div>

                <div>
                <% try{ %>
                    <h3>Request Info:</h3><%= MiscWebUtils.getReqInfo(request) %>
                <% }catch (Exception e){ %>
                    No request information is available.
                <% } %>
                </div>
            </div> <!-- contentwrap -->
            <jsp:include page="/templates/page/freemarkerTransition/footer.jsp" flush="true"/>
        </div> <!-- wrap -->
    </body>
</html>
