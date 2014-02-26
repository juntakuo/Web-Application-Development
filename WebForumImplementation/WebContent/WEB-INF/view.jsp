<!-- /* Student Name: Tsu-Hao KUo
 * andrewID: tkuo
 * Date: Mar. 2, 2013
 * Course number: 08764/15637/15437 
 */ -->


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="template-top.jsp" />

<img src="image.do?id=${photo.id}" />
<table>
	<tr>
		<td>Price:</td>
		<td><font size="4"> ${photo.prices} </font></td>
	</tr>
	<tr>
		<td>Description:</td>
		<td><font size="4"> ${photo.description} </font></td>
	</tr>
	<tr>
		<td>Date of listing:</td>
		<td><font size="4"> ${photo.listingDate} </font></td>
	</tr>
	<tr>
		<td>Seller:</td>
		<td><font size="4"> ${photo.owner} </font></td>
	</tr>
	<tr>
		<td>Seller's email:</td>
		<td><font size="4"><a href="mailto:${photo.ownerEmail}">
					${photo.ownerEmail}</a></font></td>
	</tr>
</table>


<jsp:include page="template-bottom.jsp" />
