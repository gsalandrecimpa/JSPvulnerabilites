<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="newline" value="<%= \"\n\" %>" />

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Vulnérabilité</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>

<jsp:include page="_header.jsp"></jsp:include>

<h2>Vulnérabilité</h2>

<table>
<tr>
<td>reference: </td><td><h4> ${model.getVuln().getReference()}   </h4></td>
</tr><tr>
<td>titre: </td><td><h3> ${model.getVuln().getTitre()}</h3></td>
</tr><tr>
<td>gravité: </td><td><h4><strong>${model.getVuln().getGravite()}</strong> / 4</h4></td>
</tr></table>


<table>
<tr><th>Logiciels: </th>
<td><ul>
<c:forEach var="logiciel" items="${model.getVuln().getLogiciels()}" >
	<li>${logiciel}</li>
</c:forEach>
</ul></td></tr>
</table>

<h4>   Description: </h4>
<p>${fn:replace(model.getVuln().getDescription(), newline, "<br>")}</p>
<br>

<h3>Solutions</h3>
<c:url value="/admin/vuln" var="url"/>
<table>
<tr><th>reference</th><th>titre</th><th>logiciels</th><th>description</th></tr>
<c:forEach var="sol" items="${model.getVuln().getSolutions()}" >
	<tr>
	<td>${sol.getReference()}</td>
	<td>${sol.getTitre()}</td>
	<td><ul>
	<c:forEach var="logiciel" items="${sol.getLogiciels()}" >
		<li>${logiciel}</li>
	</c:forEach>
	</ul></td>
	<td>${fn:replace(sol.getDescription(), newline, "<br>")}</td>
	</tr>
</c:forEach>
</table>


<br>
<hr>
</body>
</body>
</html>