<%-- 
    Document   : stream.jsp
    Created on : 8.11.2012, 22:13:15
    Author     : bydga
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="cs-cz" dir="ltr">
	<head>
		<meta charset="UTF-8">
		<title>Latest tweets | Be in Touch </title>
		<link rel="stylesheet" href="css/styles.css" >
		<script type="text/javascript" src="js/jquery.js"></script>
		<script type="text/javascript" src="js/script.js"></script>
	</head>
	<body>
		<div class="top-bar">
			<div class="top-bar-content">
				| <a href="stream">Tweet Stream</a> | <a href="user">All Users</a> | <a href="user?id=${user.id}">My Profile</a> | <a href="login?logout">Logout</a> | 
			</div>
		</div>
		<div class="wrapper" id="page-live-stream">

			<div class="tweet-list">
				<h1>Recent tweets</h1>
				<c:forEach items="${tweets}" var="tweet">
					<div class="tweet rounded-box">
						<div class="author-icon">
							<a href="#"><img src="user-images/<c:out value="${tweet.retweetedFrom eq null ? tweet.author.image: tweet.retweetedFrom.author.image}"/>" alt="tweet author"></a>
						</div>
						<div class="tweet-content">
							<div class="tweet-author">
								<c:choose>
									<c:when test="${tweet.retweetedFrom ne null}">
										<a class="profile-link" href="user?id=${tweet.retweetedFrom.author.id}">@${tweet.retweetedFrom.author.login}</a>
										(retweeted by <a class="profile-link" href="user?id=${tweet.author.id}">@${tweet.author.login}</a>)
									</c:when>
									<c:when test="${tweet.retweetedFrom eq null}">
										<a class="profile-link" href="user?id=${tweet.author.id}">@${tweet.author.login}</a>
									</c:when>
								</c:choose>
							</div>
							<div class="tweet-text">${tweet.text}</div>
						</div>
						<div class="cleaner"></div>
						<span class="tweet-bottom"><a class="tweet-date" href="#"><fmt:formatDate value="${tweet.published}" pattern="dd.MM.yyyy HH:mm" /></a> | ${tweet.retweetCount}x Retweeted | <a class="tweet-date" href="stream?retweet=${tweet.tweetId}">Retweet now</a></span>
					</div>
				</c:forEach>

				<div class="pager">
					<c:forEach items="${tweetsPager}" var="item">
						<c:if test="${item.active}"><span><a href="stream?page=${item.number}">${item.text}</a></span></c:if>
						<c:if test="${!item.active}"><span class="pager-curent">${item.text}</a></span></c:if>
					</c:forEach>
				</div>

			</div>

			<div class="right-menu">

				<div class="logo">
				</div>

				<div class="new-tweet rounded-box">
					<h3>Publish new tweet</h3>
					<form method="post">
						<textarea id="new-tweet-textarea" name="new-tweet-textarea">What's on your mind?</textarea><br>
						<input id="new-tweet-button" type="submit" name="new-tweet-button" value="Tweet">
					</form>
					<div class="cleaner"></div>
				</div>

				<div class="profile-info-upper">
					<div class="user-image"><img src="user-images/${user.image}" alt="picture"></div>
					<div class="user-name">${user.name} ${user.surname}<br><a class="profile-link" href="user?id=${user.id}">@${user.login}</a><br><a class="profile-link" href="user?id=${user.id}">View profile</a></div>

					<div class="cleaner"></div>
				</div>
				<div class="profile-info-lower">
					<table>
						<tr><td class="left-cell">People you follow:</td><td><a href="#">${fn:length(followings)}</a></td></tr>
						<tr><td class="left-cell">People following you:</td><td><a href="#">${fn:length(followers)}</a></td></tr>
					</table>
				</div>
			</div>
			<div class="cleaner"></div>
		</div>
	</body>
</html>