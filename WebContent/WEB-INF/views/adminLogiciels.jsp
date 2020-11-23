<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Vulnérabilités - logiciels (admin)</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>

<jsp:include page="_header.jsp"></jsp:include>

<h2>Administration des logiciels</h2>

<c:url value="/admin/logiciels" var="url"/>
<form action="${url}" method="POST">
<br>
<table>
<tr> <th>Nom du logiciel</th> </tr>
<c:forEach var="logiciel" items="${model.getLogiciels()}" >
	<tr>
	<td>${logiciel.getNom()}</td>
	<td>
	<button name="delLog" value="${logiciel.getNom()}">supprimer</button>
	</td>
	</tr>
</c:forEach>
</table>
</form>

<h2>Ajouter un logiciel</h2>
<c:url value="/admin/logiciels" var="url"/>
<form action="${url}" method="POST">
<label for="logiciel">Nom du logiciel: </label>
<br>
<input type="text" id="logiciel" name="logiciel" required maxlength="50" size="50">
<br>
<input type="submit" name = "add" value = "Valider">
<br>
</form>


</body>
</html>