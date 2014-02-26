<!-- /* Student Name: Tsu-Hao KUo
 * andrewID: tkuo
 * Date: Mar. 2, 2013
 * Course number: 08764/15637/15437 
 */ -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="template-top.jsp" />

<p style="font-size: medium">Add a new picture:</p>

<jsp:include page="error-list.jsp" />

<p>
<form method="post" action="upload.do" enctype="multipart/form-data">
	<table>
		<tr>
			<td>File:</td>
			<td colspan="2"><input type="file" name="file"
				value="${filename}" /></td>
		</tr>
		<tr>
			<td>Caption:</td>
			<td><input type="text" name="caption" value="${caption}" /></td>
			<td>(optional)</td>
		</tr>
		<tr>
			<td>Price:</td>
			<td><input type="text" name="prices" value="${prices}" /></td>
		</tr>
		<tr>
			<td>Description:</td>
			<td><input type="text" name="description" value="${description}" /></td>
		</tr>
		<tr>
			<td colspan="3" align="center"><input type="submit"
				name="button" value="Upload File" /></td>
		</tr>
	</table>
</form>
</p>
<hr />

<table>
	<c:set var="count" value="0" />
	<c:forEach var="photo" items="${photoList}">
		<c:set var="count" value="${ count+1 }" />
		<tr>
			<td>
				<form action="remove.do" method="POST">
					<input type="hidden" name="id" value="${ photo.id }" /> 
					<input type="submit" name="button" value="X" />
				</form>
			</td>
			<td valign="baseline" style="font-size: x-large">&nbsp; ${count}. &nbsp;</td>
		</tr>
		<tr>
			<th colspan=2><img src="image.do?id=${photo.id}"/></th>
		</tr>
		<tr>
			<td>Price:</td>
			<td><font size="4"><c:out value="${photo.prices}" /> </font></td>
		</tr>
		<tr>
			<td>Description:</td>
			<td>${ photo.description }</td>
		</tr>
		<tr>
			<td>Listing Date:</td>
			<td>${ photo.listingDate }</td>
		</tr>
		<tr>
			<td>Seller:</td>
			<td>${ photo.owner }</td>
		</tr>
		<tr>
			<td>Seller's Email:</td>
			<td><a href="mailto:${photo.ownerEmail}">${photo.ownerEmail}</a></td>
			<br>
		</tr>
		
	</c:forEach>
</table>
<jsp:include page="template-bottom.jsp" />
