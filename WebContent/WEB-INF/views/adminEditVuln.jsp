<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="newline" value="<%= \"\n\" %>" />

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Vulnérabilité - édition (admin)</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>

<jsp:include page="_header.jsp"></jsp:include>


<h2>Vulnérabilité - édition</h2>
<p class="erreur"> ${errText}</p><br>

<form action="${url}" method="POST">
<label for="ref">reference:</label>
<input type="text" id="ref" name="ref" 
	 maxlength="20" size="20" value="${model.getVuln().getReference()}">
<br>
<label for="titre">titre:</label>
<input type="text" id="titre" name="titre" 
	 maxlength="50" size="50" value="${model.getVuln().getTitre()}">
<br>
<label>gravité:</label>
<br>
<input type="radio" name="gravite" value="1"
	<c:if test="${model.getVuln().getGravite() == 1}"> checked </c:if>
	>1<br>
<input type="radio" name="gravite" value="2"
	<c:if test="${model.getVuln().getGravite() == 2}"> checked </c:if>
	>2<br>
<input type="radio" name="gravite" value="3"
	<c:if test="${model.getVuln().getGravite() == 3}"> checked </c:if>
	>3<br>
<input type="radio" name="gravite" value="4"
	<c:if test="${model.getVuln().getGravite() == 4}"> checked </c:if>
	>4<br>

<label for="descr">description:</label>
<br>
<textarea id="descr" name="descr" rows="10" cols="50" maxlength="1000" autofocus
	>${model.getVuln().getDescription()}</textarea>
<br>
<input type="submit" name = "save" value = "Sauver">


<table>
<tr><th>Logiciels: </th>
<td><ul>
<c:forEach var="logiciel" items="${model.getVuln().getLogiciels()}" >
	<li>${logiciel}  
	<button name="unlinkLog" value="${logiciel}">retirer</button>
	</li>
</c:forEach>
</ul></td></tr>
</table>

<table><tr>
<td>Associer un logiciel... </td>
<td>
<label for="log">nom: </label>
<select id="log" name="log">
	<c:forEach var="selectLog" items="${model.getSelectLogiciels()}" >
	<option value="${selectLog}">${selectLog}</option>
	</c:forEach>
</select>
<input type="submit" name = "linkLog" value = "ajouter">
<br>
</td></tr></table>

<br>
<h3>Solutions</h3>
<table>
<tr><th></th><th>reference</th><th>titre</th><th>logiciels</th><th>description</th></tr>
<c:forEach var="sol" items="${model.getVuln().getSolutions()}" >
	<tr>
	<td><button name=unlinkSol value="${sol.getId()}">retirer</button></td>
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

<table><tr>
<td>Associer une solution... </td>
<td>
<label for="refSol">nom: </label>
<select id="refSol" name="refSol">
	<c:forEach var="selectSolRef" items="${model.getSelectSolReferences()}" >
	<option value="${selectSolRef}">${selectSolRef}</option>
	</c:forEach>
</select>
<input type="submit" name = "linkSol" value = "ajouter">
<br>
</td></tr></table>

</form>


</body>
</html>