<%-- 
    Document   : login.jsp
    Created on : 31.10.2012, 1:45:46
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
		<title>Register | Be in Touch </title>
		<link rel="stylesheet" href="css/styles.css" >
		<script type="text/javascript" src="js/jquery.js"></script>
		<script type="text/javascript" src="js/script.js"></script>
	</head>
	<body>
		<div class="top-bar">
			<div class="top-bar-content">
				| <a href="login">Login / Register</a> | <a href="user">All Users</a> | 
			</div>
		</div>
		<div class="wrapper" id="page-login-register">

			<div class="logo"></div>
			<div class="login-form rounded-box">
				<h1>Members login</h1>
				<form id="login-form" method="post">
					<c:if test="${nickname != null}">
						Invalid login or password.
					</c:if>
					<input type="hidden" name="form-type" value="login">
					<table>
						<tr><td class="label">Login/nickname: </td><td class="field"><input type="text" value="${nickname}" name="nickname"></td></tr>
						<tr><td class="label">Password: </td><td class="field"><input type="password" name="password"></td></tr>
						<tr><td colspan="2" class="field"><input type="submit" value="Login"></td></tr>
					</table>
				</form>
			</div>

			<div class="register-form rounded-box">

				<h2>Create new profile</h2>
				<form id="register-form" method="post">
					<input type="hidden" name="form-type" value="register">
					<div id="validation-errors">

					</div>

					<c:if test="${fn:length(errors) gt 0}">
						<div id="server-errors">
							<c:forEach items="${errors}" var="error">
								<div class="error">${error}</div>
							</c:forEach>
						</div>
					</c:if>

					<table>
						<tr><td class="label">Login/nickname: </td><td class="field"><input type="text" id="login" name="login" value="${userFilled.login}"><span class="required-mark">*</span></td></tr>
						<tr><td class="label">Your image: </td><td class="field"><input id="image" type="file" name="image"><input type="text" disabled="disabled" name="image-path" id="image-path"><input value="Browse..." type="button" name="image-trigger" id="image-trigger"><div class="cleaner"></div></td></tr>
						<tr><td class="label">Password: </td><td class="field"><input id="password" type="password" name="password"><span class="required-mark">*</span></td></tr>
						<tr><td class="label">Password again: </td><td class="field"><input id="password2" type="password" name="password2"><span class="required-mark">*</span></td></tr>
						<tr><td class="label">Your name: </td><td class="field"><input type="text" name="name" value="${userFilled.name}"></td></tr>
						<tr><td class="label">Your surname: </td><td class="field"><input type="text" name="surname" value="${userFilled.surname}"></td></tr>
						<tr><td class="label">Sex: </td><td class="field"><input type="radio" <c:if test="${userFilled.sex eq 'Male' or userFilled.sex eq null}">checked</c:if> name="sex" value="Male">Male<input type="radio" name="sex" <c:if test="${userFilled.sex == 'Female'}">checked</c:if> value="Female">Female</td></tr>
						<tr><td class="label">Date of birth: </td><td class="field"><input type="text" value="<fmt:formatDate pattern="dd.MM.yyyy" value="${userFilled.birthdate}" />" name="birthdate"></td></tr>
						<tr><td class="label">Bio: </td><td class="field"><textarea name="bio" rows="3">${userFilled.bio}</textarea></td></tr>
						<tr><td class="label">${captchaA} + ${captchaB}=  </td><td class="field"><input id="captcha" type="text" name="captcha"><span class="required-mark">*</span></td></tr>
						<tr><td colspan="2"><span class="required-mark">*</span> field is required</td></tr>
						<tr><td colspan="2" class="field"><input type="submit" value="Register"></tr>
					</table>
					<input type="hidden" name="captcha-result" id="captcha-result" value="${captchaA + captchaB}">
				</form>
			</div>

			<div class="cleaner"></div>
		</div>

	</body>
</html>
