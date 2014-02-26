<!-- /* Student Name: Tsu-Hao KUo
 * andrewID: tkuo
 * Date: Mar. 2, 2013
 * Course number: 08764/15637/15437 
 */ -->

<%
    java.util.List<String> errors = (java.util.List) request.getAttribute("errors");
	if (errors != null && errors.size() > 0) {
		out.println("<p style=\"font-size:medium; color:red\">");
		for (String error : errors) {
			out.println(error+"<br/>");
		}
		out.println("</p>");
	}
%>
