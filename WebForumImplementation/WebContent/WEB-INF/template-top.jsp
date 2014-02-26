<!-- /* Student Name: Tsu-Hao KUo
 * andrewID: tkuo
 * Date: Mar. 2, 2013
 * Course number: 08764/15637/15437 
 */ -->


<html>
<head>
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="pragma" content="no-cache">
	<title>Assignment 4</title>
	<style>
		.menu-head {font-size: 18pt; font-weight: bold; color: black; }
		.menu-item {font-size: 15pt;  color: black }
    </style>
</head>

<body>
<%@ page import="databeans.User" %>

<table cellpadding="4" cellspacing="0">
    <tr>
	    <!-- Banner row across the top -->
        <td width="130" bgcolor="#99FF99" style="font-size:7px">
            <!--  img border="0" src="login2.jpg" height="75"> -->
            <!-- <img border="0" src="login3.jpg" height="75"> </td> -->
        <td bgcolor="#99FF99">&nbsp;  </td>
        <td width="500" bgcolor="#99FF99">
            <p align="center">
<%
	if (request.getAttribute("title") == null) {
%>
		        <font size="7">craigslist</font>
<%
    } else {
%>
		        <font size="5"><%=request.getAttribute("title")%></font>
<%
    }
%>
			</p>
		</td>
    </tr>
	
	<!-- Spacer row -->
	<tr>
		<td bgcolor="#99FF99" style="font-size:7px">&nbsp;</td>
		<td colspan="2" style="font-size:8px">&nbsp;</td>
	</tr>
	
	<tr>
		<td bgcolor="#99FF99" valign="top" height="500" style="font-size:10px">
			<!-- Navigation bar is one table cell down the left side -->
            <p align="left">
<%
    User user = (User) session.getAttribute("user");
	if (user == null) {
%>
				<span class="menu-item" style="font-size:20px"><a href="login.do">Login</a></span><br/>
				<span class="menu-item" style="font-size:20px"><a href="register.do">Register</a></span><br/>
				<span class="menu-item" style="font-size:20px"><a href="search.do">Item for sales</a></span><br/>
<%
    } else {
%>
				<span class="menu-head""><%=user.getFirstName()%> <%=user.getLastName()%></span><br/>
				<span class="menu-item"><a href="manage.do">Manage Your Sales</a></span><br/>
				<span class="menu-item"><a href="change-pwd.do">Change Password</a></span><br/>
				<span class="menu-item"><a href="search.do">Item for sales</a></span><br/>
				<span class="menu-item"><a href="logout.do">Logout</a></span><br/>
				<span class="menu-item">&nbsp;</span><br/>
<%
    }
%>
			</p>
		</td>
		
		<td>
			<!-- Padding (blank space) between navbar and content -->
		</td>
		<td  valign="top">
