<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="l" uri="http://shiro.apache.org/tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<c:import url="/jsp/main.jsp"><c:param name="content">
	<c:if test="${ not empty param.header }">
		<h2>${ param.header }</h2>
	</c:if>
	<c:if test="${ empty param.header }">
		<h2>Внутренняя ошибка на сервере</h2>
	</c:if>
	<hr/>

	<div class="alert alert-danger">
		${ exception }
		${ exceptionStack }
	</div>
</c:param></c:import>