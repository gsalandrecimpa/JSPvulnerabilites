<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<div>

	<hr>
	<h1>Vulnérabilités</h1>
	<h4>Bienvenue ${model.getUsername()}</h4>
	<hr>

	<c:url value="/user/vulnerabilites" var="url" />
	<a href="${url}">Liste des vulnérabilités</a>

	<c:url value="/user/logiciels" var="url" />
	<a href="${url}">Liste des Logiciels</a>

	<c:url value="/user/chgpw" var="url" />
	<a href="${url}">changer mot de passe</a>

	<c:url value="/logout" var="url" />
	<a href="${url}">se déconnecter</a>

	<hr>

</div>

<c:if test="${model.isAdmin()}">
<div>
	<c:url value="/admin/vulnerabilites" var="url" />
	<a href="${url}">Administrer vulnérabilités</a>

	<c:url value="/admin/logiciels" var="url" />
	<a href="${url}">Administrer logiciels</a>

	<c:url value="/admin/solutions" var="url" />
	<a href="${url}">Administrer solutions</a>

	<c:url value="/admin/users" var="url" />
	<a href="${url}">Administrer utilisateurs</a>

	<hr>

</div>
</c:if>

<br>
