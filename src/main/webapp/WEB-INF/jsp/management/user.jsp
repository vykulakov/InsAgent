<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="l" uri="http://shiro.apache.org/tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<c:import url="/WEB-INF/jsp/main.jsp">
<c:param name="link">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-table.min.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-multiselect.css'/>"/>
</c:param>
<c:param name="script">
	<script type="application/javascript" src="<c:url value='/js/bootstrap-table.min.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/bootstrap-table-cookie.min.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/locale/bootstrap-table-ru-RU.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/bootstrap-validator.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/bootstrap-multiselect.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/js.cookie.js'/>"></script>
	<script type="application/javascript" src="<c:url value='/js/management/user.js'/>"></script>
</c:param>
<c:param name="content">
	<h2>Управление пользователями</h2>
	<hr>

	<div id="editUserModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<form id="editUserForm" method="post" action="<c:url value='/updateUserJson.action'/>">
					<input id="userIdInput" name="user.id" type="hidden" value="0"/>
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="editUserLabel">Добавление пользователя</h4>
					</div>
					<div id="editUserBody" class="modal-body">
						<div class="form-group">
							<label for="userUsernameInput" class="control-label">Логин:</label>
							<input id="userUsernameInput" name="user.username" type="text" class="form-control" placeholder="Логин" required>
						</div>
						<div class="row">
							<div class="col-xs-6">
								<div class="form-group">
									<label for="userPasswordInput" class="control-label">Пароль:</label>
									<input id="userPasswordInput" name="user.password" type="password" class="form-control" data-password="#userIdInput" data-password-error="Необходимо ввести пароль" data-passwordminlength="#userIdInput" data-passwordminlength-error="Пароль слишком короткий" placeholder="Пароль">
									<span class="help-block">Минимум 6 символов</span>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="userPasswordInput" class="control-label">&nbsp;</label>
									<input id="userPasswordConfirmInput" name="confirm" type="password" class="form-control" data-passwordmatch="#userPasswordInput" data-passwordmatch-error="Пароли должны совпадать" placeholder="Подтверждение пароля">
									<div class="help-block with-errors"></div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="userFirstNameInput" class="control-label">Имя и фамилия:</label>
							<div class="row">
								<div class="col-xs-6">
									<input id="userFirstNameInput" name="user.firstName" type="text" class="form-control" placeholder="Имя" required>
								</div>
								<div class="col-xs-6">
									<input id="userLastNameInput" name="user.lastName" type="text" class="form-control" placeholder="Фамилия" required>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="userRoleInput" class="control-label">Роль:</label>
							<select id="userRoleInput" name="user.roles" class="form-control" data-selected="foo" data-selected-error="Необходимо выбрать роль" required>
								<option value="0">--- Не выбрана ---</option>
								<c:forEach var="role" items="${ roles }">
									<option value="${ role.idx }"><c:out value="${ role.name }"/></option>
								</c:forEach>
							</select>
							<div class="help-block with-errors"></div>
						</div>
						<div class="form-group">
							<label for="userUnitInput" class="control-label">Подразделение:</label>
							<select id="userUnitInput" name="user.unit.id" class="form-control" data-selected="bar" data-selected-error="Необходимо выбрать подразделение" required>
								<option value="0">--- Не выбрано ---</option>
								<c:forEach var="unit" items="${ units }">
									<option value="${ unit.id }"><c:out value="${ unit.name }"/></option>
								</c:forEach>
							</select>
							<div class="help-block with-errors"></div>
						</div>
						<div class="form-group">
							<label for="userCommentInput" class="control-label">Комментарий:</label>
							<textarea id="userCommentInput" name="user.comment" class="form-control"></textarea>
						</div>
					</div>
					<div class="modal-footer">
						<button id="editCloseButton" type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
						<button id="editUserButton" type="submit" class="btn btn-primary">Добавить</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div id="removeUserModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<form id="removeUserForm" method="post" action="<c:url value='/removeUserJson.action'/>">
					<input id="removeUserIdInput" name="userId" type="hidden" value="0"/>
					<div class="modal-header alert alert-danger">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="removeUserLabel">Удаление пользователя</h4>
					</div>
					<div id="removeUserBody" class="modal-body">
						Удалить выбранного пользователя?
					</div>
					<div class="modal-footer">
						<button id="removeCloseButton" type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
						<button id="removeUserButton" type="submit" class="btn btn-primary">Удалить</button>
					</div>
				</form>
			</div>
		</div>
	</div>

    <div id="usersTableAlert"></div>

    <div id="filterUserDiv" class="panel panel-default hidden">
    	<div class="panel-heading">
    		Фильтр пользователей
    	</div>
    	<div class="panel-body">
			<form id="filterUserForm" class="form-horizontal">
				<div class="form-group">
					<label for="filterUserLoginInput" class="col-sm-2 control-label">Логин:</label>
					<div class="col-sm-10">
						<input id="filterUserLoginInput" name="login" type="text" class="form-control" placeholder="Логин"/>
					</div>
				</div>
				<div class="form-group">
					<label for="filterUserNameInput" class="col-sm-2 control-label">Пользователь:</label>
					<div class="col-sm-10">
						<input id="filterUserNameInput" name="name" type="text" class="form-control" placeholder="Пользователь"/>
					</div>
				</div>
				<div class="form-group">
					<label for="filterUserUnitsInput" class="col-sm-2 control-label">Подразделения:</label>
					<div class="col-sm-10">
						<select id="filterUserUnitsInput" name="unitIds" class="form-control" multiple>
							<c:forEach var="unit" items="${ units }">
								<option value="${ unit.id }">${ unit.name }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label for="filterUserRemovedInput" class="col-sm-2 control-label">Удалённые:</label>
					<div class="col-sm-10">
						<select id="filterUserRemovedInput" name="removed" class="form-control">
							<option value="true">Отображать</option>
							<option value="false" selected="selected">Не отображать</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-2">
						<button id="filterUserSubmitButton" type="submit" class="btn btn-primary pull-right">Применить</button>
					</div>
					<div class="col-sm-10">
						<button id="filterUserClearButton" type="reset" class="btn btn-default">Очистить</button>
					</div>
				</div>
			</form>
    	</div>
    </div>

	<div id="toolbar" class="btn-group">
		<button id="openAddUserButton" type="button" class="btn btn-default">
			<i class="glyphicon glyphicon-plus"></i>
		</button>
		<button id="openEditUserButton" type="button" class="btn btn-default">
			<i class="glyphicon glyphicon-pencil"></i>
		</button>
		<button id="openRemoveUserButton" type="button" class="btn btn-default">
			<i class="glyphicon glyphicon-trash"></i>
		</button>
    </div>
	<table id="usersTable"></table>
</c:param>
</c:import>
