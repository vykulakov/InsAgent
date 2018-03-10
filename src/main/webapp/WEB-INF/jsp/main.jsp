<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="description" content="">
    <meta name="author" content="Kulakov Vyacheclav &lt;kulakov.home@gmail.com&gt;">

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap.min.css'/>"/>
    <c:out value="${ param.link }" escapeXml="false"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/main.css?v=BUILD_NUMBER'/>"/>
    <title>Новый век</title>
    <script type="text/javascript">
        var BASE_URL = '<c:url value="/"/>';
    </script>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-inner">
            <a class="navbar-brand" href="<c:url value='/'/>">Новый век</a>
            <p class="navbar-text pull-right">
                Вы вошли как <a href="<c:url value='/logout'/>" class="navbar-link"><s:authentication property="principal.name" /></a>
            </p>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-2 col-md-2 sidebar">
            <ul class="nav nav-sidebar">
                <s:authentication property="principal.items" var="items" />
                <c:forEach var="item" items="${ items }">
                    <c:if test='${ item.level == 1 }'>
                        <li class="nav-header"><c:out value='${ item.name }'/></li>
                    </c:if>
                    <c:if test='${ item.level == 2 }'>
                        <li ${ requestScope['javax.servlet.forward.request_uri'].endsWith(item.action) ? ' class="active"' : ''}>
                            <a href="<c:url value='${ item.action }'/>"><c:out value='${ item.name }'/></a></li>
                    </c:if>
                </c:forEach>
            </ul>
        </div>
        <div class="col-sm-10 col-sm-offset-3 col-md-10 col-md-offset-2">
            ${ param.content }
        </div>
    </div>
</div>
<script type="application/javascript" src="<c:url value='/js/jquery-2.1.4.js'/>"></script>
<script type="application/javascript" src="<c:url value='/js/bootstrap.js'/>"></script>
<script type="application/javascript" src="<c:url value='/js/main.js?v=BUILD_NUMBER'/>"></script>
<c:out value="${ param.script }" escapeXml="false"/>
</body>
</html>