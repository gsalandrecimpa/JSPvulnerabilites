<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Vulnérabilités</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>

<jsp:include page="_header.jsp"></jsp:include>

<div>
<h2>Liste des vulnérabilités</h2></div>

<table>
<tr><th>Reference</th><th>Gravité</th><th>Logiciels</th><th>Titre</th></tr>
<c:forEach var="vuln" items="${model.getVulnerabilites()}" >
	<tr><td>
	<c:url value="/user/vuln?id=${vuln.getId()}" var="url"/>
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

<hr>
</body>
</html>