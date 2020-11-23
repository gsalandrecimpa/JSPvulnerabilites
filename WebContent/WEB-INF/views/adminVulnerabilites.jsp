<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Vuln�rabilit�s (admin)</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>

<jsp:include page="_header.jsp"></jsp:include>

<h2> Administration des Vuln�rabilit�s</h2>

<c:url value="/admin/editVuln?new=true" var="url"/>
<a href="${url}">Nouvelle vuln�rabilit�...</a>

<c:url value="/admin/vulnerabilites" var="url"/>
<form action="${url}" method ="POST">
<table>
<tr><th></th><th>Reference</th><th>Gravit�</th><th>Logiciels</th><th>Titre</th></tr>
<c:forEach var="vuln" items="${model.getVulnerabilites()}" >
	<tr>
	<td>
	<button name="delVuln" value="${vuln.getId()}">supprimer</button>
	</td>
	<td>
	<c:url value="/admin/editVuln?id=${vuln.getId()}" var="url"/>
	<a href="${url}">${vuln.getReference()}</a>
	</td>
	<td><strong>${vuln.getGravite()}</strong> / 4</td>
	<td><ul>
	<c:forEach var="logiciel" items="${vuln.getLogiciels()}" >
		<li>${logiciel}</li>
	</c:forEach>
	</ul></td>
	<td>${vuln.getTitre()}</td>
	</tr>
</c:forEach>
</table>
</form>

<hr>
</body>
</html>