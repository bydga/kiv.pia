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
		<title>@${user.login} | Be in Touch </title>
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

			<div class="tweet-list">
				<h1>${user.login} latest tweets</h1>

				<c:if test="${fn:length(tweets) eq 0}">No tweets to display</c:if>
				<c:forEach  items="${userTweets}" var="tweet">
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


				<c:if test="${fn:length(tweets)gt 0}">
					<div class="pager">
						<div class="pager">
							<c:forEach items="${tweetsPager}" var="item">
								<c:if test="${item.active}"><span><a href="user?id=${user.id}&page=${item.number}">${item.text}</a></span></c:if>
								<c:if test="${!item.active}"><span class="pager-curent">${item.text}</a></span></c:if>
							</c:forEach>
						</div>
					</div>
				</c:if>
			</div>
			<div class="right-menu">

				<div class="profile-info-upper">
					<div class="user-image"><img src="user-images/${user.image}" alt="picture"></div>
					<div class="user-name">
						${user.name} ${user.surname}<br><a class="profile-link" href="user?id=${user.id}">@${user.login}</a>
						<c:if test="${loggedUser ne null and friends}">
							- <a class="follow-link" href="user?unfollow=${user.id}">Unfollow</a>
						</c:if>
						<c:if test="${loggedUser ne null and !friends and loggedUser.id ne user.id}">
							- <a class="follow-link" href="user?follow=${user.id}">Follow</a>
						</c:if>
					</div>
					<div class="cleaner"></div>
				</div>
				<div class="profile-info-lower">
					<div>${user.sex}<c:if test="${user.age ne null}">, ${user.age} years</c:if></div>
					<c:if test="${fn:length(user.bio) gt 0}"><div><strong>Bio:</strong> ${user.bio}</div></c:if>
					</div>

					<div class="friend-list">
						<h3>${fn:length(followers)} Followers</h3>

					<c:forEach items="${followers}" var="follower">
						<div class="profile-card-zebra2 rounded-box">
							<div class="user-image"><img src="user-images/${follower.image}" alt="picture"></div>
							<div class="user-name">${follower.name} ${follower.surname}<br><a class="profile-link" href="user?id=${follower.id}">@${follower.login}</a></div>
							<div class="cleaner"></div>
						</div>

					</c:forEach>

					<h3>${fn:length(followings)} Followings</h3>

					<c:forEach items="${followings}" var="following">
						<div class="profile-card-zebra2 rounded-box">
							<div class="user-image"><img src="user-images/${following.image}" alt="picture"></div>
							<div class="user-name">${following.name} ${following.surname}<br><a class="profile-link" href="user?id=${following.id}">@${following.login}</a></div>
							<div class="cleaner"></div>
						</div>

					</c:forEach>

				</div>
			</div>

			<div class="cleaner"></div>
		</div>
	</body>
</html>