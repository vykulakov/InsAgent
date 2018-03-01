'use strict';

var filter = {};
var sorter = {};

$(function() {
	$('[data-toggle="tooltip"]').tooltip({
		container: 'body'
	});

	var bsosTableObj = $('#bsosTable');
	bsosTableObj.bootstrapTable({
		url: '',
		queryParams: function(params) {
			$.extend(params, filter);

			return params;
		},
		responseHandler: function(response) {
			$('#bsosTableAlert').children().remove();

			var errors = checkError(response);
			if(errors !== undefined) {
				var alertMsg = '' +
				'<div id="bsosTableAlert" class="alert alert-danger fade in">' +
				'    <a href="#" class="close" data-dismiss="alert">&times;</a>' +
				'    <strong>При получении данных возникли ошибки:</strong><br/>';
				for(var i = 0, l = errors.length; i < l; i++) {
					alertMsg += errors[i] + '<br/>';
				}
				alertMsg += '</div>';

				$('#bsosTableAlert').append(alertMsg);

				return [];
			}

			return response;
		},
		clickToSelect: true,
		singleSelect: false,
		checkboxHeader: true,
		columns: [{
			checkbox: true
		}, {
			field: 'created',
			title: 'Создан',
			titleTooltip: 'Дата и время создания БСО',
			sortable: true
		}, {
			field: 'series',
			title: 'Серия',
			sortable: true
		}, {
			field: 'number',
			title: 'Номер',
			sortable: true
		}, {
			field: 'node',
			title: 'Узел',
			titleTooltip: 'Этап, на котором в настоящее время находится БСО',
			sortable: true,
			formatter: function(node) {
				if(node === null) {
					return null;
				} else {
					return node.name;
				}
			}
		}, {
			field: 'unit',
			title: 'Подразд.',
			titleTooltip: 'Подразделение, в котором в настоящее время находится БСО',
			sortable: true,
			formatter: function(unit) {
				if(unit === null) {
					return null;
				} else {
					return unit.name;
				}
			}
		}, {
			field: 'issuedDate',
			title: 'Выдан',
			titleTooltip: 'Дата и время выдачи БСО страхователю',
			sortable: true
		}, {
			field: 'issuedBy',
			title: 'Выдал',
			titleTooltip: 'Пользователь, который выдал БСО страхователю',
			sortable: true,
			formatter: function(issuedBy) {
				if(issuedBy === null) {
					return null;
				} else {
					return issuedBy.firstName + ' ' + issuedBy.lastName;
				}
			},
			visible: false
		}, {
			field: 'issuedUnit',
			title: 'Выдал',
			titleTooltip: 'Подразделение, в котором БСО был выдан страхователю',
			sortable: true,
			formatter: function(issuedUnit) {
				if(issuedUnit === null) {
					return null;
				} else {
					return issuedUnit.name;
				}
			},
			visible: false
		}, {
			field: 'corruptedDate',
			title: 'Испорчен',
			titleTooltip: 'Дата и время отметки БСО испорченным',
			sortable: true
		}, {
			field: 'corruptedBy',
			title: 'Испортил',
			titleTooltip: 'Пользователь, который отметил БСО испорченным',
			sortable: true,
			formatter: function(corruptedBy) {
				if(corruptedBy === null) {
					return null;
				} else {
					return corruptedBy.firstName + ' ' + corruptedBy.lastName;
				}
			},
			visible: false
		}, {
			field: 'corruptedUnit',
			title: 'Испортил',
			titleTooltip: 'Подразделение, в котором БСО был отмечен испорченным',
			sortable: true,
			formatter: function(corruptedUnit) {
				if(corruptedUnit === null) {
					return null;
				} else {
					return corruptedUnit.name;
				}
			},
			visible: false
		}, {
			field: 'registeredDate',
			title: 'Регистр.',
			titleTooltip: 'Дата и время регистрации БСО в системе',
			sortable: true
		}, {
			field: 'registeredBy',
			title: 'Регистр.',
			titleTooltip: 'Пользователь, который зарегистрировал БСО в системе',
			sortable: true,
			formatter: function(registeredBy) {
				if(registeredBy === null) {
					return null;
				} else {
					return registeredBy.firstName + ' ' + registeredBy.lastName;
				}
			},
			visible: false
		}, {
			field: 'registeredUnit',
			title: 'Регистр.',
			titleTooltip: 'Подразделение, в котором БСО был зарегистрирован в системе',
			sortable: true,
			formatter: function(registeredUnit) {
				if(registeredUnit === null) {
					return null;
				} else {
					return registeredUnit.name;
				}
			},
			visible: false
		}, {
			field: 'insured',
			title: 'Страхователь',
			sortable: true
		}, {
			field: 'premium',
			title: 'Премия',
			titleTooltip: 'Страховая премия',
			sortable: true
		}],
		search: true,
		showColumns: true,
		showRefresh: true,
		toolbar: '#toolbar',
		pagination: true,
		sidePagination: 'server',
		pageSize: 20,
		pageList: [10, 20, 50, 100],
		stateSave: true,
		stateSaveIdTable: 'bsosArchivedTable'
	});
	bsosTableObj.on('sort.bs.table', function(e, sort, order) {
		sorter.sort = sort;
		sorter.order = order;
    });

	/**
	 * Добавляем фильтр к таблице.
	 */
	bsosTableObj.parents('.bootstrap-table').find('.fixed-table-toolbar').append('' +
			'<div class="columns columns-left btn-group pull-right">' + 
			'    <button id="openFilterBsoButton" title="Фильтр БСО" name="filter" type="button" class="btn btn-default">' + 
			'        <i id="openFilterBsoIcon" class="glyphicon glyphicon-menu-up icon-menu-up"></i>' +
			'    </button>' +
			'</div>');
	$('#openFilterBsoButton').on('click', function() {
		$('#filterBsoDiv').toggleClass('hidden');
		$('#openFilterBsoIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
	});
	$('#filterBsoCreatedFromInput, #filterBsoCreatedToInput, #filterBsoIssuedFromInput, #filterBsoIssuedToInput, #filterBsoCorruptedFromInput, #filterBsoCorruptedToInput, #filterBsoRegisteredFromInput, #filterBsoRegisteredToInput').datetimepicker({
		locale: 'ru',
		useCurrent: false,
		showTodayButton: true,
		showClear: true
	});
    $("#filterBsoCreatedFromInput").on("dp.change", function(e) {
        $('#filterBsoCreatedToInput').data("DateTimePicker").minDate(e.date);
    });
    $("#filterBsoCreatedToInput").on("dp.change", function(e) {
        $('#filterBsoCreatedFromInput').data("DateTimePicker").maxDate(e.date);
    });
    $("#filterBsoIssuedFromInput").on("dp.change", function(e) {
        $('#filterBsoIssuedToInput').data("DateTimePicker").minDate(e.date);
    });
    $("#filterBsoIssuedToInput").on("dp.change", function(e) {
        $('#filterBsoIssuedFromInput').data("DateTimePicker").maxDate(e.date);
    });
    $("#filterBsoCorruptedFromInput").on("dp.change", function(e) {
    	$('#filterBsoCorruptedToInput').data("DateTimePicker").minDate(e.date);
    });
    $("#filterBsoCorruptedToInput").on("dp.change", function(e) {
    	$('#filterBsoCorruptedFromInput').data("DateTimePicker").maxDate(e.date);
    });
    $("#filterBsoRegisteredFromInput").on("dp.change", function(e) {
    	$('#filterBsoRegisteredToInput').data("DateTimePicker").minDate(e.date);
    });
    $("#filterBsoRegisteredToInput").on("dp.change", function(e) {
    	$('#filterBsoRegisteredFromInput').data("DateTimePicker").maxDate(e.date);
    });
    var filterBsoFormObj = $('#filterBsoForm');
	var cookie = Cookies.getJSON('bsosArchivedFilter');
	if(!!cookie && !!cookie.filter) {
		filter = {};

		filterBsoFormObj.find(':input').each(function() {
			if(this.name && cookie[this.name]) {
				$(this).val(cookie[this.name]);
				if(this.name == 'filter.createdFrom' || this.name == 'filter.createdTo' || this.name == 'filter.issuedFrom' || this.name == 'filter.issuedTo' || this.name == 'filter.corruptedFrom' || this.name == 'filter.corruptedTo' || this.name == 'filter.registeredFrom' || this.name == 'filter.registeredTo') {
					$(this).trigger('change');
				}
			}
    	});

		var nodeIndex = 0;
		var unitIndex = 0;
		filterBsoFormObj.serializeArray().map(function(param) {
			if(param.name == 'filter.nodes') {
				filter['filter.nodes[' + (nodeIndex++) + '].id'] = param.value;
				return;
			}
			if(param.name == 'filter.units') {
				filter['filter.units[' + (unitIndex++) + '].id'] = param.value;
				return;
			}
			if(param.value !== '') {
				filter[param.name] = param.value;
				return;
			}
		});

		$('#openFilterBsoButton').removeClass('btn-default').addClass('btn-warning');

		bsosTableObj.bootstrapTable('refresh', {url: 'listBsosArchivedJson.action'});
	} else {
		bsosTableObj.bootstrapTable('refresh', {url: 'listBsosArchivedJson.action'});
	}
	filterBsoFormObj.on('submit', function(e) {
		// Форму отправлять не нужно.
		e.preventDefault();

		filter = {};

		var cookie = {};
		var nodeIndex = 0;
		var unitIndex = 0;
		filterBsoFormObj.serializeArray().map(function(param) {
			if(param.value !== '') {
				cookie.filter = true;

				filter[param.name] = param.value;
				cookie[param.name] = param.value;

				return;
			}
		});

		Cookies.set('bsosArchivedFilter', cookie);

		$('#filterBsoDiv').toggleClass('hidden');
		$('#openFilterBsoIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
		if(!!cookie && !!cookie.filter) {
			$('#openFilterBsoButton').removeClass('btn-default').addClass('btn-warning');
			
			bsosTableObj.bootstrapTable('refresh');
		}
	});
	filterBsoFormObj.on('reset', function(e) {
		// Форму отправлять не нужно.
		e.preventDefault();

		filter = {};
		filterBsoFormObj.find(':input').each(function() {
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
		});

		Cookies.remove('bsosArchivedFilter');

		$('#filterBsoDiv').toggleClass('hidden');
		$('#openFilterBsoIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
		$('#openFilterBsoButton').removeClass('btn-warning').addClass('btn-default');

		bsosTableObj.bootstrapTable('refresh');
	});

	/**
	 * Добавляем экспорт к таблице.
	 */
	bsosTableObj.parents('.bootstrap-table').find('.fixed-table-toolbar').find('.columns-right').append('' +
			'<button id="openExportBsoButton" title="Экспорт в Excel" name="export" type="button" data-toggle="filterBsoDiv" class="btn btn-default">' + 
			'    <i class="glyphicon glyphicon-export icon-share"></i>' +
			'</button>');
	$('#openExportBsoButton').on('click', function() {
		$('#exportBsoModalBody').find('.alert').remove();

		$('#exportBsoModal').modal({});
	});
	var exportBsoFormObj = $('#exportBsoForm');
	exportBsoFormObj.on('submit', function(e) {
		// Форма отправляется через AJAX, поэтому стандартную отправку нужно отключить.
		e.preventDefault();

		if(!!!sorter.sort || !!!sorter.order) {
			sorter.sort = 'series';
			sorter.order = 'asc';
		}

		window.open(BASE_URL + 'exportBsoArchivedJournal.action?' + $.param(filter) + '&' + $.param(sorter));

		$('#exportBsoModal').modal('hide');
		bsosTableObj.bootstrapTable('refresh');
	});
});
