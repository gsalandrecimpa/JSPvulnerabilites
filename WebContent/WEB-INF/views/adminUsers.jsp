<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Vulnérabilités - users (admin)</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>

<jsp:include page="_header.jsp"></jsp:include>

<h2>Liste des utilisateurs</h2>
<c:url value="/admin/users" var="url"/>
<form action="${url}" method="POST">
<table>
<tr>
	<th>username</th>
	<th>Role</th>
</tr>
<c:forEach var="user" items="${model.getUsers()}">
	<tr>
	<td>${user.getUsername()}</td>
	<td>${user.getRole()}</td>
	<td>
	<button name="resetpwd" value="${user.getUsername()}">reinit. mdp</button>
	</td>
	<td>
	<button name="chgrole" value="${user.getUsername()}">change role</button>
	</td>
	<td>
	<button name="remove" value="${user.getUsername()}">retirer</button>
	</td>
	</tr>
</c:forEach>
</table>
</form>


<hr>
</body>
</html>