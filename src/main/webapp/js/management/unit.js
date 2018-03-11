'use strict';

var filter = {};

$(function() {
	var $table = $('#unitsTable');
	var $alert = $('#unitsTableAlert');

	$table.bootstrapTable({
		url: '',
		queryParams: function(params) {
			$.extend(params, filter);

			return params;
		},
		responseHandler: function(response) {
            $alert.children().remove();

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

                $alert.append(alertMsg);

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
			field: 'name',
			title: 'Название',
			sortable: true
		}, {
			field: 'typeName',
			title: 'Тип',
			sortable: true
		}, {
			field: 'cityName',
			title: 'Город',
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
		stateSaveIdTable: 'unitsTable'
	});

	$('#openAddUnitButton').on('click', function() {
		$('#unitIdInput').val(0);
		$('#unitNameInput').val('');
		$('#unitTypeInput').val(0);
		$('#unitCityInput').val(0);
		$('#unitCommentInput').val('');

		$('#editUnitLabel').text('Добавление подразделения');
		$('#editUnitButton').text('Добавить');

		$('#editUnitModal').modal({});
	});

	$('#openEditUnitButton').on('click', function() {
		$('#editUnitBody').find('.alert').remove();

		var row = $('#unitsTable').bootstrapTable('getSelections');

		var l = row.length;
		if(l === 0) {
			alert('Необходимо выбрать подразделение.');
			return;
		}

		var unit = row[0];
		$('#unitIdInput').val(unit.id);
		$('#unitNameInput').val(unit.name);
		$('#unitTypeInput').val(unit.type.id);
		$('#unitCityInput').val(unit.city.id);
		$('#unitCommentInput').val(unit.comment);

		$('#editUnitLabel').text('Редактирование подразделения');
		$('#editUnitButton').text('Сохранить');

		$('#editUnitModal').modal({});
	});

	$('#openRemoveUnitButton').on('click', function() {
		$('#removeUnitBody').find('.alert').remove();

		var row = $('#unitsTable').bootstrapTable('getSelections');

		var l = row.length;
		if(l === 0) {
			alert('Необходимо выбрать пользователя.');
			return;
		}

		var unit = row[0];
		$('#removeUnitIdInput').val(unit.id);

		$('#removeUnitModal').modal({});
	});

	$('#editUnitForm').validator({
		custom: {
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
			selected: "Значение не выбрано"
		}
	}).on('submit', function(e) {
		if(e.isDefaultPrevented()) {
			return;
		}

		// Форма отправляется через AJAX, поэтому стандартную отправку нужно отключить.
		e.preventDefault();

		var $form = $('#editUnitForm');
		var $body = $('#editUnitBody');
		$.getJSON($form.attr('action'), $form.serialize(), function(response) {
			$body.find('.alert').remove();

			var errors = checkError(response);
			if(errors !== undefined) {
				var alert = '' +
				'<div class="alert alert-danger fade in">' +
				'    <a href="#" class="close" data-dismiss="alert">&times;</a>' +
				'    <strong>При добавлении/обновлении подразделения возникли ошибки:</strong><br/>';
				for(var i = 0, l = errors.length; i < l; i++) {
					alert += errors[i] + '<br/>';
				}
				alert += '</div>';

                $body.prepend(alert);

				return;
			}

			$('#editUnitModal').modal('hide');
			$('#unitsTable').bootstrapTable('refresh');
		});
	});

	$('#removeUnitForm').on('submit', function(e) {
		// Форма отправляется через AJAX, поэтому стандартную отправку нужно отключить.
		e.preventDefault();

		var form = $('#removeUnitForm');
		$.getJSON(form.attr('action'), form.serialize(), function(response) {
			$('#removeUnitBody').find('.alert').remove();

			var errors = checkError(response);
			if(errors !== undefined) {
				var alert = '' +
				'<div class="alert alert-danger fade in">' +
				'    <a href="#" class="close" data-dismiss="alert">&times;</a>' +
				'    <strong>При удалении подразделения возникли ошибки:</strong><br/>';
				for(var i = 0, l = errors.length; i < l; i++) {
					alert += errors[i] + '<br/>';
				}
				alert += '</div>';

				$('#removeUnitBody').prepend(alert);

				return;
			}

			$('#removeUnitModal').modal('hide');
			$('#unitsTable').bootstrapTable('refresh');
		});
	});

	/**
	 * Добавляем фильтр к таблице.
	 */
	$table.parents('.bootstrap-table').find('.fixed-table-toolbar').append('' +
			'<div class="columns columns-left btn-group pull-right">' +
			'    <button id="openFilterUnitButton" title="Фильтр подразделений" name="filter" type="button" class="btn btn-default">' +
			'        <i id="openFilterUnitIcon" class="glyphicon glyphicon-menu-up icon-menu-up"></i>' +
			'    </button>' +
			'</div>');
	$('#openFilterUnitButton').on('click', function() {
		$('#filterUnitDiv').toggleClass('hidden');
		$('#openFilterUnitIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
	});
	$('#filterUnitTypesInput').multiselect({
		buttonWidth: '100%',
		numberDisplayed: 10,
		allSelectedText: 'Выбраны все доступные типы',
		nonSelectedText: 'Не выбраны',
		includeSelectAllOption: true,
		selectAllText: 'Выбрать всё'
	});
	$('#filterUnitCitiesInput').multiselect({
		buttonWidth: '100%',
		numberDisplayed: 10,
		allSelectedText: 'Выбраны все доступные города',
		nonSelectedText: 'Не выбраны',
		includeSelectAllOption: true,
		selectAllText: 'Выбрать всё'
	});
	var filterUnitFormObj = $('#filterUnitForm');
	var cookie = Cookies.getJSON('unitsFilter');
	if(!!cookie && cookie.filter) {
		filter = {};

		filterUnitFormObj.find(':input').each(function() {
			if(this.name && cookie[this.name]) {
				$(this).val(cookie[this.name]);
				if(this.name === 'types' || this.name === 'cities') {
					$(this).multiselect('refresh');
				}
			}
    	});

		var typeIndex = 0;
		var cityIndex = 0;
		filterUnitFormObj.serializeArray().map(function(param) {
			if(param.name === 'types') {
				filter['types[' + (typeIndex++) + '].id'] = param.value;
				return;
			}
			if(param.name === 'cities') {
				filter['cities[' + (cityIndex++) + '].id'] = param.value;
				return;
			}
			if(param.value !== '') {
				filter[param.name] = param.value;
				return;
			}
		});

		$('#openFilterUnitButton').removeClass('btn-default').addClass('btn-warning');

		$table.bootstrapTable('refresh', {url: '/management/units'});
	} else {
		$table.bootstrapTable('refresh', {url: '/management/units'});
	}
	filterUnitFormObj.on('submit', function(e) {
		e.preventDefault();

		filter = {};

		var cookie = {};
		var typeIndex = 0;
		var cityIndex = 0;
		filterUnitFormObj.serializeArray().map(function(param) {
			if(param.name === 'types') {
				cookie.filter = true;

				filter['types[' + (typeIndex++) + '].id'] = param.value;
				if(cookie['types'] === undefined) {
					cookie['types'] = [param.value];
				} else {
					cookie['types'].push(param.value);
				}

				return;
			}
			if(param.name === 'cities') {
				cookie.filter = true;

				filter['cities[' + (cityIndex++) + '].id'] = param.value;
				if(cookie['cities'] === undefined) {
					cookie['cities'] = [param.value];
				} else {
					cookie['cities'].push(param.value);
				}

				return;
			}
			if(param.name === 'removed' && param.value === 'true') {
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

		Cookies.set('unitsFilter', cookie);

		$('#filterUnitDiv').toggleClass('hidden');
		$('#openFilterUnitIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
		if(!!cookie && cookie.filter) {
			$('#openFilterUnitButton').removeClass('btn-default').addClass('btn-warning');

			$table.bootstrapTable('refresh');
		}
	});
	filterUnitFormObj.on('reset', function(e) {
		// Форму отправлять не нужно.
		e.preventDefault();

		filter = {};
		filterUnitFormObj.find(':input').each(function() {
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
			$('#filterUnitTypesInput').multiselect('refresh');
			$('#filterUnitCitiesInput').multiselect('refresh');
			$('#filterUnitRemovedInput').val('false');
		});

		Cookies.remove('unitsFilter');

		$('#filterUnitDiv').toggleClass('hidden');
		$('#openFilterUnitIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
		$('#openFilterUnitButton').removeClass('btn-warning').addClass('btn-default');

		$table.bootstrapTable('refresh');
	});
});
