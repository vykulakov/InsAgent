<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/WEB-INF/jsp/main.jsp">
<c:param name="link">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-table.min.css'/>"/>
</c:param>
<c:param name="script">
	<script type="application/javascript" src="<c:url value='/js/bootstrap-table.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/locale/bootstrap-table-ru-RU.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/bootstrap-validator.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/journal/act.js'/>"></script>
</c:param>
<c:param name="content">
	<h2>Журнал актов</h2>
	<hr>

    <div id="actsTableAlert"></div>
	<table id="actsTable"></table>
</c:param>
</c:import>
