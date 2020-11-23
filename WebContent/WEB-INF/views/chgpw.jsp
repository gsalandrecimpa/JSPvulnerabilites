<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Vulnérabilités - Changer mdp</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>

<h1>Vulnérabilités</h1>
<h3>Changer de mot de passe</h3>
<h4>utilisateur: ${model.getUsername()}</h4>

<p class="erreur"> ${errText}</p><br>

<c:url value="/user/chgpw" var="url"/>
<form action="${url}" method="POST">
<div>
<label for="oldpwd">Mot de passe actuel: </label>
<input type="password" id="oldpwd" name="oldpwd" autofocus required maxlength="20" size="20">
</div>
<div>
<label for="pwd">Saisir nouveau mot de passe: </label>
<input type="password" id="pwd" name="pwd" required maxlength="20" size="20">
</div>
<div>
<label for="repwd">Confirmer nouveau mot de passe: </label>
<input type="password" id="repwd" name="repwd" required maxlength="20" size="20">
</div>
<div>
<input type="submit" name = "submit" value = "Valider">
</div>
</form>

</body>
</html>