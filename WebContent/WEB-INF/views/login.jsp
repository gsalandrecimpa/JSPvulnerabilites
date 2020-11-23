<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Vulnérabilités - Connexion</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
</head>

<body>

<h1>Vulnérabilités  </h1>

<c:url value="/register" var="url"/>
<a href="${url}">s'enregistrer</a>

<br><br><h3>Connexion:</h3>
<p class="erreur"> ${errText}</p><br>

<c:url value="/login" var="url"/>
<form action="${url}" method="POST">
<div>
<label for="login">Nom d'utilisateur: </label>
<input type="text" id="login" name="login" value="${model.getUsername()}" 
	autofocus required maxlength="20" size="20">
</div>
<div>
<label for="pwd">Mot de passe: </label>
<input type="password" id="pwd" name="pwd" required maxlength="20" size="20">
</div>
<div>
<input type="submit" name = "submit" value = "Valider">
</div>
</form>

</body>
</html>