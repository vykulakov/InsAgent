<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container">
	<form class="form-signin" method="post" action="${ authUrl }">
		<input type="hidden" name="authDo" value="true"/>

		<c:if test="${ not empty authError }">
			<div class="alert alert-danger fade in">
				<a href="#" class="close" data-dismiss="alert">&times;</a>
				<strong>Ошибка входа</strong><br/>
				${authError}
			</div>
		</c:if>

		<h2 class="form-signin-heading">Вход в систему</h2>
		<label for="inputUser" class="sr-only">Имя пользователя</label>
		<input id="inputUser" type="text" name="authUser" class="form-control" placeholder="Имя пользователя" value="${ authUser }" required autofocus/>
		<label for="inputPass" class="sr-only">Пароль</label>
		<input id="inputPass" type="password" name="authPass" class="form-control" placeholder="Пароль" value="" required/>
		<div class="checkbox">
			<label><input type="checkbox" name="authRememberMe" value="true"/> Запомнить меня?</label>
		</div>

		<button type="submit" class="btn btn-lg btn-primary btn-block">Войти</button>
	</form>
</div>
