<%-- 
    Document   : user.jsp
    Created on : 8.11.2012, 22:13:15
    Author     : bydga
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="cs-cz" dir="ltr">
	<head>
		<meta charset="UTF-8">
		<title>All Users | Be in Touch </title>
		<link rel="stylesheet" href="css/styles.css" >
		<script type="text/javascript" src="js/jquery.js"></script>
		<script type="text/javascript" src="js/script.js"></script>
	</head>
	<body>
		<div class="top-bar">
			<div class="top-bar-content">
				<c:if test="${loggedUser ne null}">
					| <a href="stream">Tweet Stream</a> | <a href="user">All Users</a> | <a href="user?id=${loggedUser.id}">My Profile</a> | <a href="login?logout">Logout</a> | 
				</c:if>
				<c:if test="${loggedUser eq null}">
					| <a href="login">Login / Register</a> | <a href="user">All Users</a> | 
				</c:if>
			</div>
		</div>
		<div class="wrapper" id="page-profile">

			<!--<div class="right-menu">-->


			<div class="users-list">
				<h3>Total of ${fn:length(users)} users</h3>

				<c:forEach items="${users}" var="user">
					<div class="profile-card-zebra2 rounded-box">
						<div class="user-image"><img src="user-images/${user.image}" alt="picture"></div>
						<div class="user-name">${user.name} ${user.surname}<br><a class="profile-link" href="user?id=${user.id}">@${user.login}</a></div>
						<div class="cleaner"></div>
					</div>

				</c:forEach>

			</div>
			<!--</div>-->

			<div class="cleaner"></div>
		</div>
	</body>
</html>