<!-- /* Student Name: Tsu-Hao KUo
 * andrewID: tkuo
 * Date: Mar. 2, 2013
 * Course number: 08764/15637/15437 
 */ -->


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="databeans.Photo" %>

<jsp:include page="template-top.jsp" />
<jsp:include page="error-list.jsp" />
<form action="search.do" method="get">
	<table>
		<tr>
			<td>Search: </td>
			<td>
				<input type="text" name="keywords" value="${form.keywords}"/>
				<input type="submit" name="button" value="search"/>
			</td>
		</tr>
	</table>
</form>
<jsp:include page="list.jsp" />
<jsp:include page="template-bottom.jsp" />