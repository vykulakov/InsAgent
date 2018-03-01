<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="description" content="">
    <meta name="author" content="Kulakov Vyacheclav &lt;kulakov.home@gmail.com&gt;">

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap.min.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/login.css?v=BUILD_NUMBER'/>"/>
    <title>Новый век</title>
</head>
<body>
<div class="container">
    <form class="form-signin" method="post" action="<c:url value='/login'/>">
        <c:if test="${param.error != null}">
            <div class="alert alert-danger fade in">
                <a href="#" class="close" data-dismiss="alert">&times;</a>
                <strong>Ошибка авторизации:</strong>
                <c:if test="${SPRING_SECURITY_LAST_EXCEPTION != null}">
                    Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
                </c:if>
            </div>
        </c:if>
        <c:if test="${param.logout != null}">
            <div class="alert alert-success fade in">
                <a href="#" class="close" data-dismiss="alert">&times;</a>
                <strong>Выход успешно выполнен</strong>
            </div>
        </c:if>

        <h2 class="form-signin-heading">Вход в систему</h2>
        <input type="hidden"  name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <label for="username" class="sr-only">Имя пользователя</label>
        <input id="username" type="text" name="username" class="form-control" placeholder="Имя пользователя"
               value="${ param.username }" required autofocus/>
        <label for="password" class="sr-only">Пароль</label>
        <input id="password" type="password" name="password" class="form-control" placeholder="Пароль" value=""
               required/>
        <div class="checkbox">
            <label><input type="checkbox" id="remember-me" name="remember-me" value="true"/> Запомнить меня?</label>
        </div>

        <button type="submit" class="btn btn-lg btn-primary btn-block">Войти</button>
    </form>
</div>
<script type="application/javascript" src="<c:url value='/js/jquery-2.1.4.js'/>"></script>
<script type="application/javascript" src="<c:url value='/js/bootstrap.js'/>"></script>
</body>
</html>
