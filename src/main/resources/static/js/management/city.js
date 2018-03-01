'use strict';

var filter = {};

$(function() {
	var citiesTableObj = $('#citiesTable');
	citiesTableObj.bootstrapTable({
		url: '',
		queryParams: function(params) {
			$.extend(params, filter);

			return params;
		},
		responseHandler: function(response) {
			$('#citiesTableAlert').children().remove();

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

				$('#citiesTableAlert').append(alertMsg);

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
		stateSaveIdTable: 'citiesTable'
	});

	$('#openAddCityButton').on('click', function() {
		$('#cityIdInput').val(0);
		$('#cityNameInput').val('');
		$('#cityCommentInput').val('');

		$('#editCityLabel').text('Добавление города');
		$('#editCityButton').text('Добавить');

		$('#editCityModal').modal({});
	});

	$('#openEditCityButton').on('click', function() {
		$('#editCityBody').find('.alert').remove();

		var row = $('#citiesTable').bootstrapTable('getSelections');

		var l = row.length;
		if(l === 0) {
			alert('Необходимо выбрать город.');
			return;
		}

		var city = row[0];
		$('#cityIdInput').val(city.id);
		$('#cityNameInput').val(city.name);
		$('#cityCommentInput').val(city.comment);

		$('#editCityLabel').text('Редактирование города');
		$('#editCityButton').text('Сохранить');

		$('#editCityModal').modal({});
	});

	$('#openRemoveCityButton').on('click', function() {
		$('#removeCityBody').find('.alert').remove();

		var row = $('#citiesTable').bootstrapTable('getSelections');

		var l = row.length;
		if(l === 0) {
			alert('Необходимо выбрать город.');
			return;
		}

		var city = row[0];
		$('#removeCityIdInput').val(city.id);

		$('#removeCityModal').modal({});
	});

	$('#editCityForm').validator({
		errors: {
			match: "Значения полей не совпадают",
			minlength: "Значение слишком короткое"
		}
	}).on('submit', function(e) {
		if(e.isDefaultPrevented()) {
			return;
		}

		// Форма отправляется через AJAX, поэтому стандартную отправку нужно отключить.
		e.preventDefault();
		
		var form = $('#editCityForm');
		$.getJSON(form.attr('action'), form.serialize(), function(response) {
			$('#editCityBody').find('.alert').remove();

			var errors = checkError(response);
			if(errors !== undefined) {
				var alert = '' +
				'<div class="alert alert-danger fade in">' +
				'    <a href="#" class="close" data-dismiss="alert">&times;</a>' +
				'    <strong>При добавлении/обновлении города возникли ошибки:</strong><br/>';
				for(var i = 0, l = errors.length; i < l; i++) {
					alert += errors[i] + '<br/>';
				}
				alert += '</div>';
				
				$('#editCityBody').prepend(alert);
				
				return;
			}

			$('#editCityModal').modal('hide');
			$('#citiesTable').bootstrapTable('refresh');
		});
	});

	$('#removeCityForm').on('submit', function(e) {
		// Форма отправляется через AJAX, поэтому стандартную отправку нужно отключить.
		e.preventDefault();

		var form = $('#removeCityForm');
		$.getJSON(form.attr('action'), form.serialize(), function(response) {
			$('#removeCityBody').find('.alert').remove();

			var errors = checkError(response);
			if(errors !== undefined) {
				var alert = '' +
				'<div class="alert alert-danger fade in">' +
				'    <a href="#" class="close" data-dismiss="alert">&times;</a>' +
				'    <strong>При удалении города возникли ошибки:</strong><br/>';
				for(var i = 0, l = errors.length; i < l; i++) {
					alert += errors[i] + '<br/>';
				}
				alert += '</div>';

				$('#removeCityBody').prepend(alert);

				return;
			}

			$('#removeCityModal').modal('hide');
			$('#citiesTable').bootstrapTable('refresh');
		});
	});

	/**
	 * Добавляем фильтр к таблице.
	 */
	citiesTableObj.parents('.bootstrap-table').find('.fixed-table-toolbar').append('' +
			'<div class="columns columns-left btn-group pull-right">' + 
			'    <button id="openFilterCityButton" title="Фильтр городов" name="filter" type="button" class="btn btn-default">' + 
			'        <i id="openFilterCityIcon" class="glyphicon glyphicon-menu-up icon-menu-up"></i>' +
			'    </button>' +
			'</div>');
	$('#openFilterCityButton').on('click', function() {
		$('#filterCityDiv').toggleClass('hidden');
		$('#openFilterCityIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
	});
	var filterCityFormObj = $('#filterCityForm');
	var cookie = Cookies.getJSON('citiesFilter');
	if(!!cookie && !!cookie.filter) {
		filter = {};

		filterCityFormObj.find(':input').each(function() {
			if(this.name && cookie[this.name]) {
				$(this).val(cookie[this.name]);
			}
    	});

		filterCityFormObj.serializeArray().map(function(param) {
			if(param.value !== '') {
				filter[param.name] = param.value;
				return;
			}
		});

		$('#openFilterCityButton').removeClass('btn-default').addClass('btn-warning');

		citiesTableObj.bootstrapTable('refresh', {url: 'getCitiesJson.action'});
	} else {
		citiesTableObj.bootstrapTable('refresh', {url: 'getCitiesJson.action'});
	}
	filterCityFormObj.on('submit', function(e) {
		// Форму отправлять не нужно.
		e.preventDefault();

		filter = {};

		var cookie = {};
		filterCityFormObj.serializeArray().map(function(param) {
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

		Cookies.set('citiesFilter', cookie);

		$('#filterCityDiv').toggleClass('hidden');
		$('#openFilterCityIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
		if(!!cookie && !!cookie.filter) {
			$('#openFilterCityButton').removeClass('btn-default').addClass('btn-warning');
			
			citiesTableObj.bootstrapTable('refresh');
		}
	});
	filterCityFormObj.on('reset', function(e) {
		// Форму отправлять не нужно.
		e.preventDefault();

		filter = {};
		filterCityFormObj.find(':input').each(function() {
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
			$('#filterCityRemovedInput').val('false');
		});

		Cookies.remove('citiesFilter');

		$('#filterCityDiv').toggleClass('hidden');
		$('#openFilterCityIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
		$('#openFilterCityButton').removeClass('btn-warning').addClass('btn-default');

		citiesTableObj.bootstrapTable('refresh');
	});
});
