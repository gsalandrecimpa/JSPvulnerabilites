<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Solution - édition (admin)</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>

<jsp:include page="_header.jsp"></jsp:include>

<h2>Solution - édition</h2>
<p class="erreur"> ${errText}</p><br>

<c:url value="/admin/editSol" var="url"/>
<form id="form1" action="${url}" method="POST">
<label for="ref">reference:</label>
<input type="text" id="ref" name="ref" 
	 maxlength="20" size="20" value="${model.getSol().getReference()}">
<br>
<label for="titre">titre:</label>
<input type="text" id="titre" name="titre" 
	 maxlength="50" size="50" value="${model.getSol().getTitre()}">
<br>

<label for="descr">description:</label>
<br>
<textarea id="descr" form="form1" name="descr" rows="10" cols="50" maxlength="1000" autofocus
	>${model.getSol().getDescription()}</textarea>
<br>
<input type="submit" name="save" value="Sauver">

<c:url value="/admin/editSol" var="url"/>
<table>
<tr><th>Logiciels: </th>
<td><ul>
<c:forEach var="logiciel" items="${model.getSol().getLogiciels()}" >
	<li>${logiciel}  
	<button name=unlinkLog value="${logiciel}">retirer</button>
	</li>
</c:forEach>
</ul></td></tr>
</table>

<table><tr><td>
<h5>Associer un logiciel... </h5>
</td><td>
<label for="log">nom: </label>
<select id="log" name="log">
	<c:forEach var="selectLog" items="${model.getSelectLogiciels()}" >
		<option value="${selectLog}">${selectLog}</option>
	</c:forEach>
</select>
<input type="submit" name = "linkLog" value = "ajouter">
</td></tr>
</table>
</form>

</body>
</html>