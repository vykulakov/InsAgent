<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="s" uri="http://shiro.apache.org/tags"%>

<!DOCTYPE html>
<html lang="ru">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">

	    <meta name="description" content="">
    	<meta name="author" content="Kulakov Vyacheclav kulakov.home@gmail.com">

		<!-- Bootstrap -->
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap.min.css'/>"/>
		<c:out value="${ param.link }" escapeXml="false"/>
		<s:guest>
			<link rel="stylesheet" type="text/css" href="<c:url value='/css/login.css'/>"/>
		</s:guest>
		<s:user>
			<link rel="stylesheet" type="text/css" href="<c:url value='/css/main.css'/>"/>
		</s:user>
		<title>Автодом Страхование</title>
		<script type="text/javascript">
			var BASE_URL = '<c:url value="/"/>';
		</script>
	</head>
	<body>
		<s:guest>
			<jsp:include page="login.jsp"/>
		</s:guest>
		<s:user>
			<nav class="navbar navbar-inverse navbar-fixed-top">
				<div class="container-fluid">
					<div class="navbar-inner">
						<a class="navbar-brand" href="<c:url value='/'/>">Автодом Страхование</a>
						<p class="navbar-text pull-right">
							Вы вошли как <a href="<c:url value='logout'/>" class="navbar-link"><s:principal property="firstName"/> <s:principal property="lastName"/></a>
						</p>
					</div>
				</div>
			</nav>

			<div class="container-fluid">
				<div class="row">
					<div class="col-sm-2 col-md-2 sidebar">
						<ul class="nav nav-sidebar">
							<c:forEach var="item" items="${ menu }">
								<c:if test='${ item.level == 1 }'>
									<li class="nav-header"><c:out value='${ item.name }'/></li>
								</c:if>
								<c:if test='${ item.level == 2 }'>
									<li ${ requestScope['javax.servlet.forward.request_uri'].endsWith(item.action) ? ' class="active"' : ''}><a href="<c:url value='${ item.action }'/>"><c:out value='${ item.name }'/></a></li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
					<div class="col-sm-10 col-sm-offset-3 col-md-10 col-md-offset-2">
						${ param.content }
					</div>
				</div>
			</div>
		</s:user>
	    <script type="application/javascript" src="<c:url value='/js/jquery-2.1.4.js'/>"></script>
	    <script type="application/javascript" src="<c:url value='/js/bootstrap.js'/>"></script>
	    <script type="application/javascript" src="<c:url value='/js/main.js'/>"></script>
   		<c:out value="${ param.script }" escapeXml="false"/>
	</body>
</html>