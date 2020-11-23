<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="newline" value="<%= \"\n\" %>" />

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Vulnérabilités - solutions (admin)</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>

<jsp:include page="_header.jsp"></jsp:include>

<h2>Administration des solutions</h2>

<c:url value="/admin/editSol?new=true" var="url"/>
<a href="${url}">Nouvelle solution...</a>

<c:url value="/admin/solutions" var="url"/>
<form action="${url}" method ="POST">
<table>
<tr><th></th><th>Reference</th><th>Titre</th><th>Description</th></tr>
<c:forEach var="sol" items="${model.getSolutions()}" >
	<tr>
	<td>
	<button name="delSol" value="${sol.getId()}">supprimer</button>
	</td>
	<td><c:url value="/admin/editSol?id=${sol.getId()}" var="url"/>
	<a href="${url}">${sol.getReference()}</a></td>
	<td>${sol.getTitre()}</td>
	<td>${fn:replace(sol.getDescription(), newline, "<br>")}</td>
	</tr>
</c:forEach>
</table>
</form>

<hr>
</body>
</html>