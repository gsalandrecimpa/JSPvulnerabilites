<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Vulnérabilités - Logiciels</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>

<jsp:include page="_header.jsp"></jsp:include>

<h2>Liste des logiciels</h2>

<c:url value="/user/logiciels" var="url"/>
<form action="${url}" method="POST">
<div>
<input type="submit" name = "update" value = "Valider la sélection">
</div>
<div>
<table>
<tr> <th>Sélection:</th><td>   <td><th>Nom du logiciel</th> </tr>
<c:forEach var="logiciel" items="${model.getLogiciels()}" >
	<tr>
	<td>
	<input type= "checkbox" name= "logiciels" value="${logiciel.getNom()}"
		<c:if test="${logiciel.isLinkedToUser()}"> checked </c:if> >
	</td><td>   <td>
	<td>${logiciel.getNom()}</td>
	</tr>
</c:forEach>
</table>
</div>
</form>

<hr>
</body>
</html>