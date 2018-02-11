'use strict';

var filter = {};

$(function() {
	var usersTableObj = $('#usersTable');
	usersTableObj.bootstrapTable({
		url: '',
		queryParams: function(params) {
			$.extend(params, filter);

			return params;
		},
		responseHandler: function(response) {
			$('#usersTableAlert').children().remove();

			var errors = checkError(response);
			if(errors !== undefined) {
				var alertMsg = '' +
				'<div class="alert alert-danger fade in">' +
				'    <a href="#" class="close" data-dismiss="alert">&times;</a>' +
				'    <strong>При получении данных возникли ошибки:</strong><br/>';
				for(var i = 0, l = errors.length; i < l; i++) {
					alertMsg += errors[i] + '<br/>';
				}
				alertMsg += '</div>';

				$('#usersTableAlert').append(alertMsg);

				return [];
			}

			return response;
		},
		clickToSelect: true,
		singleSelect: true,
		checkboxHeader: false,
		columns: [{
			checkbox: true
		}, {
			field: 'id',
			title: 'id',
			sortable: true
		}, {
			field: 'username',
			title: 'Логин',
			sortable: true
		}, {
			field: 'firstName',
			title: 'Имя',
			sortable: true
		}, {
			field: 'lastName',
			title: 'Фамилия',
			sortable: true
		}, {
			field: 'unit',
			title: 'Подразделение',
			sortable: true,
			formatter: function(unit) {
				return unit.name;
			}
		}, {
			field: 'lastAuth',
			title: 'Последний вход',
			sortable: true
		}],
		rowStyle: function(row) {
			if(row.removed) {
				return {
					classes: 'danger'
				};
			}

			return {};
		},
		search: true,
		showRefresh: true,
		toolbar: '#toolbar',
		pagination: true,
		sidePagination: 'server',
		pageSize: 10,
		pageList: [5, 10, 20, 50],
		stateSave: true,
		stateSaveIdTable: 'usersTable'
	});

	$('#openAddUserButton').on('click', function() {
		$('#userIdInput').val(0);
		$('#userUsernameInput').val('');
		$('#userPasswordInput').val('');
		$('#userPasswordConfirmInput').val('');
		$('#userFirstNameInput').val('');
		$('#userLastNameInput').val('');
		$('#userRoleInput').val(0);
		$('#userUnitInput').val(0);
		$('#userCommentInput').val('');

		$('#editUserLabel').text('Добавление пользователя');
		$('#editUserButton').text('Добавить');

		$('#editUserModal').modal({});
	});

	$('#openEditUserButton').on('click', function() {
		$('#editUserBody').find('.alert').remove();

		var row = $('#usersTable').bootstrapTable('getSelections');

		var l = row.length;
		if(l === 0) {
			alert('Необходимо выбрать пользователя.');
			return;
		}

		var user = row[0];
		$('#userIdInput').val(user.id);
		$('#userUsernameInput').val(user.username);
		$('#userFirstNameInput').val(user.firstName);
		$('#userLastNameInput').val(user.lastName);
		$('#userRoleInput').val(user.roles[0]);
		$('#userUnitInput').val(user.unit.id);
		$('#userCommentInput').val(user.comment);

		$('#editUserLabel').text('Редактирование пользователя');
		$('#editUserButton').text('Сохранить');

		$('#editUserModal').modal({});
	});

	$('#openRemoveUserButton').on('click', function() {
		$('#removeUserBody').find('.alert').remove();

		var row = $('#usersTable').bootstrapTable('getSelections');

		var l = row.length;
		if(l === 0) {
			alert('Необходимо выбрать пользователя.');
			return;
		}

		var user = row[0];
		$('#removeUserIdInput').val(user.id);

		$('#removeUserModal').modal({});
	});

	$('#editUserForm').validator({
		custom: {
			password: function(element) {
				var target = element.data('password');
				if(parseInt($(target).val()) === 0 && element.val().trim().length === 0) {
					return false;
				}

				return true;
			},
			passwordminlength: function(element) {
				var target = element.data('passwordminlength');
				if(parseInt($(target).val()) === 0 && element.val().trim().length !== 0 && element.val().trim().length < 6) {
					return false;
				}

				return true;
			},
			passwordmatch: function(element) {
				var target = element.data('passwordmatch');
				if($(target).val() !== element.val()) {
					return false;
				}

				return true;
			},
			selected: function(element) {
				if(element.val() == 0) {
					return false;
				}
	
				return true;
			}
		},
		errors: {
			match: "Значения полей не совпадают",
			minlength: "Значение слишком короткое",
			password: "Пароль не указан",
			passwordminlength: "Пароль слишком короткий",
			passwordmatch: "Пароли должны совпадать",
			selected: "Значение не выбрано"
		}
	}).on('submit', function(e) {
		if(e.isDefaultPrevented()) {
			return;
		}

		// Форма отправляется через AJAX, поэтому стандартную отправку нужно отключить.
		e.preventDefault();
		
		var form = $('#editUserForm');
		$.getJSON(form.attr('action'), form.serialize(), function(response) {
			$('#editUserBody').find('.alert').remove();

			var errors = checkError(response);
			if(errors !== undefined) {
				var alert = '' +
				'<div class="alert alert-danger fade in">' +
				'    <a href="#" class="close" data-dismiss="alert">&times;</a>' +
				'    <strong>При добавлении/обновлении пользователя возникли ошибки:</strong><br/>';
				for(var i = 0, l = errors.length; i < l; i++) {
					alert += errors[i] + '<br/>';
				}
				alert += '</div>';
				
				$('#editUserBody').prepend(alert);
				
				return;
			}

			$('#editUserModal').modal('hide');
			$('#usersTable').bootstrapTable('refresh');
		});
	});

	$('#removeUserForm').on('submit', function(e) {
		// Форма отправляется через AJAX, поэтому стандартную отправку нужно отключить.
		e.preventDefault();
		
		var form = $('#removeUserForm');
		$.getJSON(form.attr('action'), form.serialize(), function(response) {
			$('#removeUserBody').find('.alert').remove();

			var errors = checkError(response);
			if(errors !== undefined) {
				var alert = '' +
				'<div class="alert alert-danger fade in">' +
				'    <a href="#" class="close" data-dismiss="alert">&times;</a>' +
				'    <strong>При удалении пользователя возникли ошибки:</strong><br/>';
				for(var i = 0, l = errors.length; i < l; i++) {
					alert += errors[i] + '<br/>';
				}
				alert += '</div>';
				
				$('#removeUserBody').prepend(alert);
				
				return;
			}

			$('#removeUserModal').modal('hide');
			$('#usersTable').bootstrapTable('refresh');
		});
	});

	/**
	 * Добавляем фильтр к таблице.
	 */
	usersTableObj.parents('.bootstrap-table').find('.fixed-table-toolbar').append('' +
			'<div class="columns columns-left btn-group pull-right">' + 
			'    <button id="openFilterUserButton" title="Фильтр пользователей" name="filter" type="button" class="btn btn-default">' + 
			'        <i id="openFilterUserIcon" class="glyphicon glyphicon-menu-up icon-menu-up"></i>' +
			'    </button>' +
			'</div>');
	$('#openFilterUserButton').on('click', function() {
		$('#filterUserDiv').toggleClass('hidden');
		$('#openFilterUserIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
	});
	$('#filterUserUnitsInput').multiselect({
		buttonWidth: '100%',
		numberDisplayed: 10,
		allSelectedText: 'Выбраны все доступные подразделения',
		nonSelectedText: 'Не выбраны',
		includeSelectAllOption: true,
		selectAllText: 'Выбрать всё'
	});
	var filterUserFormObj = $('#filterUserForm');
	var cookie = Cookies.getJSON('usersFilter');
	if(!!cookie && !!cookie.filter) {
		filter = {};

		filterUserFormObj.find(':input').each(function() {
			if(this.name && cookie[this.name]) {
				$(this).val(cookie[this.name]);
				if(this.name == 'filter.units') {
					$(this).multiselect('refresh');
				}
			}
    	});

		var unitIndex = 0;
		filterUserFormObj.serializeArray().map(function(param) {
			if(param.name == 'filter.units') {
				filter['filter.units[' + (unitIndex++) + '].id'] = param.value;
				return;
			}
			if(param.value !== '') {
				filter[param.name] = param.value;
				return;
			}
		});

		$('#openFilterUserButton').removeClass('btn-default').addClass('btn-warning');

		usersTableObj.bootstrapTable('refresh', {url: 'getUsersJson.action'});
	} else {
		usersTableObj.bootstrapTable('refresh', {url: 'getUsersJson.action'});
	}
	filterUserFormObj.on('submit', function(e) {
		// Форму отправлять не нужно.
		e.preventDefault();

		filter = {};

		var cookie = {};
		var unitIndex = 0;
		filterUserFormObj.serializeArray().map(function(param) {
			if(param.name == 'filter.units') {
				cookie.filter = true;

				filter['filter.units[' + (unitIndex++) + '].id'] = param.value;
				if(cookie['filter.units'] === undefined) {
					cookie['filter.units'] = [param.value];
				} else {
					cookie['filter.units'].push(param.value);
				}

				return;
			}
			if(param.name == 'filter.removed' && param.value == 'true') {
				cookie.filter = true;

				filter[param.name] = param.value;
				cookie[param.name] = param.value;

				return;
			}
			if(param.value !== '') {
				cookie.filter = true;

				filter[param.name] = param.value;
				cookie[param.name] = param.value;

				return;
			}
		});

		Cookies.set('usersFilter', cookie);

		$('#filterUserDiv').toggleClass('hidden');
		$('#openFilterUserIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
		if(!!cookie && !!cookie.filter) {
			$('#openFilterUserButton').removeClass('btn-default').addClass('btn-warning');
			
			usersTableObj.bootstrapTable('refresh');
		}
	});
	filterUserFormObj.on('reset', function(e) {
		// Форму отправлять не нужно.
		e.preventDefault();

		filter = {};
		filterUserFormObj.find(':input').each(function() {
			switch(this.type) {
				case 'password':
				case 'select-multiple':
				case 'select-one':
				case 'text':
				case 'textarea':
					$(this).val('');
					break;
				case 'checkbox':
				case 'radio':
					this.checked = false;
			}
			$('#filterUserUnitsInput').multiselect('refresh');
			$('#filterUserRemovedInput').val('false');
		});

		Cookies.remove('usersFilter');

		$('#filterUserDiv').toggleClass('hidden');
		$('#openFilterUserIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
		$('#openFilterUserButton').removeClass('btn-warning').addClass('btn-default');

		usersTableObj.bootstrapTable('refresh');
	});
});
