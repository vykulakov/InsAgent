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
				'<div class="alert alert-danger fade in">' +
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
		stateSaveIdTable: 'bsosTable'
	});
	bsosTableObj.on('sort.bs.table', function(e, sort, order) {
		sorter.sort = sort;
		sorter.order = order;
    });

	$('#actPacksTable').bootstrapTable({
		clickToSelect: true,
		singleSelect: false,
		checkboxHeader: true,
		columns: [{
			checkbox: true,
			width: 35
		}, {
			field: 'id',
			title: 'id',
			visible: false
		}, {
			field: 'series',
			title: 'Серия',
			sortable: true
		}, {
			field: 'numberFrom',
			title: 'Номер с',
			sortable: true
		}, {
			field: 'numberTo',
			title: 'Номер по',
			sortable: true
		}, {
			field: 'amount',
			title: 'Количество',
			sortable: true
		}, {
			field: 'operate',
			title: '',
			align: 'center',
			width: 35,
			events: {
				'click .remove': function(e, value, row, index) {
					console.log("e:     ", e);
					console.log("value: ", value);
					console.log("row:   ", row);
					console.log("index: ", index);
					$('#actPacksTable').bootstrapTable('remove', {
						field: 'id',
						values: [row.series + ":" + row.numberFrom + ':' + row.numberTo]
					});
				}
			},
			formatter: function(value, row, index) {
				console.log("value: ", value);
				console.log("row:   ", row);
				console.log("index: ", index);
				return '' +
					'<a class="remove" href="javascript:void(0)" title="Remove">' +
					'    <i class="glyphicon glyphicon-remove"></i>' +
					'</a>';
			}
		}],
		height: 200
	});

	$('#actToolbar button').on('click', function() {
		var t = $(this);

		var actTypeId = t.val();
		var actTypeIdx =  t.data('typeIdx');
		var actTypeFullName = t.data('originalTitle');

		$('#actModalTitle').html(actTypeFullName);
		$('#actTypeInput').val(actTypeId);
		$('#actTypeIdxInput').val(actTypeIdx);
		// Удаляем старые опции узла отправителя (пункт "Не выбран" не трогаем).
		$('#actSenderInput .option').remove();
		// Удаляем старые опции узла получателя (пункт "Не выбран" не трогаем).
		$('#actRecipientInput .option').remove();

		var packs = [];
		var series = '';
		var numberFrom = 0;
		var numberTo = 0;
		var bsos = bsosTableObj.bootstrapTable('getAllSelections').sort(function(a, b) {
			var r = a.series.localeCompare(b.series);
			if(r === 0) {
				if(a.number > b.number) {
					return 1;
				}
				if(a.number < b.number) {
					return -1;
				}
				return 0;
			}

			return r;
		});
		$.each(bsos, function(index, bso) {
			if(series === '') {
				series = bso.series;
				numberFrom = bso.number;
				numberTo = bso.number;
				return true;
			}
			if(series !== bso.series || numberTo + 1 < bso.number) {
				packs.push({
					id: series + ":" + numberFrom + ':' + numberTo,
					series: series,
					numberFrom: numberFrom,
					numberTo: numberTo,
					amount: (numberTo - numberFrom + 1)
				});
				series = bso.series;
				numberFrom = bso.number;
				numberTo = bso.number;
				return true;
			}
			
			numberTo = bso.number;
		});
		if(series !== '') {
			packs.push({
				id: series + ":" + numberFrom + ':' + numberTo,
				series: series,
				numberFrom: numberFrom,
				numberTo: numberTo,
				amount: (numberTo - numberFrom + 1)
			});
		}

		$('#actModalAlert').remove();

		$.getJSON(BASE_URL + 'getActJson.action', {
			actTypeId: actTypeId
		}, function(response) {
			var errors = checkError(response);
			if(errors !== undefined) {
				var alertMsg = '' +
				'<div id="actModalAlert" class="alert alert-danger fade in">' +
				'    <a href="#" class="close" data-dismiss="alert">&times;</a>' +
				'    <strong>При получении данных возникли ошибки:</strong><br/>';
				for(var i = 0, l = errors.length; i < l; i++) {
					alertMsg += errors[i] + '<br/>';
				}
				alertMsg += '</div>';

				$('#actModalBody').prepend(alertMsg);
				$('#actModal').modal({});

				return;
			}

			// Сохраняем данные для изменения опций узла получателя.
			$('#actSenderInput').data('links', response.links);
			$('#actSenderInput').data('itemsFrom', response.itemsFrom);
			$('#actSenderInput').data('itemsTo', response.itemsTo);
			// Прописываем сразу все возможные опции узла отправителя, чтобы пользователь мог выбрать отправную точку для акта.
			if(response.itemsFrom.length > 0) {
				$.each(response.itemsFrom, function(index, item) {
					$('<option class="option" value="' + item.id + '">' + item.name + '</option>')
						.data('item', item)
						.appendTo($('#actSenderInput'));
				});
				// Если доступных опций нет, то выбор остаётся заблокированным. Такого быть не должно.
				$('#actSenderInput').prop('disabled', false);
				// Если доступная опция всего одна (например, заведение чистых БСО в системе), то сразу выбираем её.
				if(response.itemsFrom.length == 1) {
					$('#actSenderInput').val(response.itemsFrom[0].id);
					// Бросаем событие, чтобы обновить список значений узла получателя.
					$('#actSenderInput').trigger('change');
				}
			}

			// Удаляем старыне сообщения об ошибках.
			$('#packAddSubAlert').remove();

			// Заполняем таблицу пачками БСО, если они выбраны в журнале.
			switch(actTypeIdx) {
				case 'input':
					$('#actPacksTable').bootstrapTable('removeAll');
					break;
				case 'transfer':
				case 'output':
					$('#actPacksTable').bootstrapTable('load', packs);
					break;
				default:
					alert('Неизвестный тип акта!');
			}

			$('#actCommentInput').val('');
			$('#packSeriesInput').prop('disabled', false).val('');
			$('#packNumberFromInput').prop('disabled', false).val('');
			$('#packNumberToInput').prop('disabled', false).val('');
			$('#packAmountInput').val('');
			$('#packAddButton').prop('disabled', false).val('');

			$('#actModal').modal({});
		});
	});

	$('#packAddButton').on('click', function() {
		var actTypeIdx = $('#actTypeIdxInput').val();

		var series = $('#packSeriesInput').val();
		var numberFrom = parseInt($('#packNumberFromInput').val());
		var numberTo = parseInt($('#packNumberToInput').val());
		var amount = numberTo - numberFrom + 1;

		if(series === '') {
			$('#packSeriesInput').parent().addClass('has-error');
			alert('Необходимо ввести серию');
			return;
		}

		if(isNaN(numberFrom)) {
			$('#packNumberFromInput').parent().addClass('has-error');
			alert('В поле "Номер с" необходимо ввести число');
			return;
		}

		if(isNaN(numberTo)) {
			$('#packNumberToInput').parent().addClass('has-error');
			alert('В поле "Номер по" необходимо ввести число');
			return;
		}

		if(amount < 1) {
			alert('Введён неправильный диапазон номеров');
			return;
		}

		var error = false;
		var packs = $('#actPacksTable').bootstrapTable('getData');
		$.each(packs, function(index, pack) {
			if(series == pack.series && (
				numberFrom <= pack.numberFrom && pack.numberFrom <= numberTo
			) || (
				numberFrom <= pack.numberTo && pack.numberTo <= numberTo
			)) {
				error = true;
				return false;
			}
		});
		if(error) {
			alert('Введённый диапазон БСО пересекается с уже добавленным диапазоном.');
			return;
		}

		// При заведении БСО НЕ ДОЛДНЫ существовать, при передаче и выводе - ДОЛЖНЫ существовать.
		var mustExists = 0;
		switch(actTypeIdx) {
			case 'input':
				mustExists = 0;
				break;
			case 'transfer':
			case 'output':
				mustExists = 1;
				break;
			default:
				alert('Неизвестный тип акта!');
				return;
		}

		$.getJSON(BASE_URL + 'checkPackJson.action', {
			'pack.id': 0,
			'pack.series': series,
			'pack.numberFrom': numberFrom,
			'pack.numberTo': numberTo,
			'pack.amount': amount,
			'mustExists': mustExists
		}, function(response) {
			$('#packAddSubAlert').remove();

			var errors = checkError(response);
			if(errors !== undefined) {
				var alertMsg = '' +
				'<div id="packAddSubAlert" class="alert alert-danger fade in">' +
				'    <a href="#" class="close" data-dismiss="alert">&times;</a>' +
				'    <strong>При получении данных возникли ошибки:</strong><br/>';
				for(var i = 0, l = errors.length; i < l; i++) {
					alertMsg += errors[i] + '<br/>';
				}
				alertMsg += '</div>';

				$('#packAddAlert').prepend(alertMsg);

				return;
			}

			$('#actPacksTable').bootstrapTable('insertRow', {
				index: packs.length,
				row: {
					id: series + ':' + numberFrom + ':' + numberTo,
					series: series,
					numberFrom: numberFrom,
					numberTo: numberTo,
					amount: amount
				}
			});

			$('#packNumberFromInput, #packNumberToInput, #packAmountInput').val('');

			$('#actPacksTable').parents('.form-group').removeClass('has-error');
			$('#actPacksTable').parents('.form-group').find('.with-errors').text('');
		});
	});

	var actFormObj = $('#actForm');
	actFormObj.on('submit', function(e) {
		// Форма отправляется через AJAX, поэтому стандартную отправку нужно отключить.
		e.preventDefault();

		var error = false;

		var typeObj = $('#actTypeInput');

		var senderObj = $('#actSenderInput');
		if(!senderObj.prop('disabled') && senderObj.val() === '0') {
			error = true;
			senderObj.parent().addClass('has-error');
			senderObj.siblings('.with-errors').text(senderObj.data('selectedError'));
		}

		var recipientObj = $('#actRecipientInput');
		if(!recipientObj.prop('disabled') && recipientObj.val() === '0') {
			error = true;
			recipientObj.parent().addClass('has-error');
			recipientObj.siblings('.with-errors').text(recipientObj.data('selectedError'));
		}

		var tableObj = $('#actPacksTable');
		if(tableObj.bootstrapTable('getData').length === 0) {
			error = true;
			tableObj.parents('.form-group').addClass('has-error');
			tableObj.parents('.form-group').find('.with-errors').text(tableObj.data('tableError'));
		}

		if(error) {
			return;
		}

		var itemFrom = senderObj.find(':selected').data('item');
		var itemTo = recipientObj.find(':selected').data('item');
		var params = {
			'act.id': 0,
			'act.type.id': typeObj.val(),
			'itemFrom.id': itemFrom.id,
			'itemFrom.name': itemFrom.name,
			'itemFrom.nodeTypeId': itemFrom.nodeTypeId,
			'itemTo.id': itemTo.id,
			'itemTo.name': itemTo.name,
			'itemTo.nodeTypeId': itemTo.nodeTypeId
		};
		$.each(itemFrom.nodeIds, function(index, nodeId) {
			params['itemFrom.nodeIds[' + index + ']'] = nodeId;
		});
		$.each(itemTo.nodeIds, function(index, nodeId) {
			params['itemTo.nodeIds[' + index + ']'] = nodeId;
		});
		$.each(tableObj.bootstrapTable('getData'), function(index, pack) {
			params['act.packs[' + index + '].series'] = pack.series;
			params['act.packs[' + index + '].numberFrom'] = pack.numberFrom;
			params['act.packs[' + index + '].numberTo'] = pack.numberTo;
			params['act.packs[' + index + '].amount'] = pack.amount;
		});
		$.post(actFormObj.attr('action'), params, function(response) {
			actFormObj.find('.alert').remove();

			var errors = checkError(response);
			if(errors !== undefined) {
				var alertMsg = '' +
				'<div id="actModalAlert" class="alert alert-danger fade in">' +
				'    <a href="#" class="close" data-dismiss="alert">&times;</a>' +
				'    <strong>При создании акта возникли ошибки:</strong><br/>';
				for(var i = 0, l = errors.length; i < l; i++) {
					alertMsg += errors[i] + '<br/>';
				}
				alertMsg += '</div>';

				$('#actModalBody').prepend(alertMsg);

				return;
			}

			$('#actModal').modal('hide');
			bsosTableObj.bootstrapTable('refresh');
		});
	});


	$('#actionToolbar button').on('click', function() {
		var bsos = bsosTableObj.bootstrapTable('getAllSelections');
		if(bsos.length === 0) {
			alert('Необходимо выбрать БСО.');
			return;
		}
		if(bsos.length > 1) {
			alert('Необходимо выбрать только один БСО.');
			return;
		}

		var t = $(this);

		var bsoId = bsos[0].id;
		var nodeId = bsos[0].node.id;
		var actionId = t.val();
		var actionIdx = t.data('actionIdx');
		var actionFullName = t.data('originalTitle');

		$('#actionModalTitle').html(actionFullName);
		$('#bsoIdInput').val(bsoId);
		$('#actionIdInput').val(actionId);
		$('#actionIdxInput').val(actionIdx);

		$('#actionModalAlert').remove();

		$('#actionModalBody').children().addClass('hidden');

		$.getJSON(BASE_URL + 'checkBsoJson.action', {
			nodeId: nodeId,
			actionId: actionId
		}, function(response) {
			var errors = checkError(response);
			if(errors !== undefined) {
				var alertMsg = '' +
				'<div id="actionModalAlert" class="alert alert-danger fade in" style="margin-bottom: 0;">' +
				'    <a href="#" class="close" data-dismiss="alert">&times;</a>' +
				'    <strong>Невозможно выполнить действие:</strong><br/>';
				for(var i = 0, l = errors.length; i < l; i++) {
					alertMsg += errors[i] + '<br/>';
				}
				alertMsg += '</div>';

				$('#actionModalBody').prepend(alertMsg);
				$('#actionModal').modal({});

				return;
			}

			// Отображаем секцию для выбранного действия.
			$('#' + actionIdx + 'ModalBody').removeClass('hidden');

			// Разблокируем элементы ввода, если нет ошибок.
			$('#actionInsuredInput').prop('disabled', false).val('');
			$('#actionPremiumInput').prop('disabled', false).val('');
			$('#actionSubmitButton').prop('disabled', false);

			$('#actionModal').modal({});
		});
	});

	var actionFormObj = $('#actionForm');
	actionFormObj.on('submit', function(e) {
		// Форма отправляется через AJAX, поэтому стандартную отправку нужно отключить.
		e.preventDefault();

		var error = false;

		var bsoId = $('#bsoIdInput').val();
		var actionId = $('#actionIdInput').val();
		var actionIdx = $('#actionIdxInput').val();

		var insuredObj = $('#actionInsuredInput');
		var premiumObj = $('#actionPremiumInput');
		if(actionIdx == 'issue') {
			if(insuredObj.val() === '') {
				error = true;
				insuredObj.parent().addClass('has-error');
				insuredObj.siblings('.with-errors').text(insuredObj.data('requiredError'));
			}

			if(premiumObj.val() === '') {
				error = true;
				premiumObj.parent().addClass('has-error');
				premiumObj.siblings('.with-errors').text(premiumObj.data('requiredError'));
			}
			if(premiumObj.val() !== '' && !!!premiumObj.val().match('^\\d+[.,]?\\d*$')) {
				error = true;
				premiumObj.parent().addClass('has-error');
				premiumObj.siblings('.with-errors').text(premiumObj.data('formatError'));
			}
		}

		if(actionIdx == 'register') {
		}

		if(error) {
			return;
		}

		$('#actionModalAlert').remove();

		$.post(actionFormObj.attr('action'), {
			bsoId: bsoId,
			actionId: actionId,
			insured: insuredObj.val(),
			premium: premiumObj.val().replace(',', '.')
		}, function(response) {
			var errors = checkError(response);
			if(errors !== undefined) {
				var alertMsg = '' +
				'<div id="actionModalAlert" class="alert alert-danger fade in">' +
				'    <a href="#" class="close" data-dismiss="alert">&times;</a>' +
				'    <strong>При выполнении действия над БСО возникли ошибки:</strong><br/>';
				for(var i = 0, l = errors.length; i < l; i++) {
					alertMsg += errors[i] + '<br/>';
				}
				alertMsg += '</div>';

				$('#actionModalBody').prepend(alertMsg);

				return;
			}

			$('#actionModal').modal('hide');
			bsosTableObj.bootstrapTable('refresh');
		});
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
	$('#filterBsoNodesInput').multiselect({
		buttonWidth: '100%',
		numberDisplayed: 10,
		allSelectedText: 'Выбраны все доступные узлы',
		nonSelectedText: 'Не выбраны',
		includeSelectAllOption: true,
		selectAllText: 'Выбрать всё'
	});
	$('#filterBsoUnitsInput').multiselect({
		buttonWidth: '100%',
		numberDisplayed: 10,
		allSelectedText: 'Выбраны все доступные подразделения',
		nonSelectedText: 'Не выбраны',
		includeSelectAllOption: true,
		selectAllText: 'Выбрать всё'
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
	var cookie = Cookies.getJSON('bsosFilter');
	if(!!cookie && !!cookie.filter) {
		filter = {};

		filterBsoFormObj.find(':input').each(function() {
			if(this.name && cookie[this.name]) {
				$(this).val(cookie[this.name]);
				if(this.name == 'filter.nodes' || this.name == 'filter.units') {
					$(this).multiselect('refresh');
				}
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

		bsosTableObj.bootstrapTable('refresh', {url: 'listBsosJson.action'});
	} else {
		bsosTableObj.bootstrapTable('refresh', {url: 'listBsosJson.action'});
	}
	filterBsoFormObj.on('submit', function(e) {
		// Форму отправлять не нужно.
		e.preventDefault();

		filter = {};

		var cookie = {};
		var nodeIndex = 0;
		var unitIndex = 0;
		filterBsoFormObj.serializeArray().map(function(param) {
			if(param.name == 'filter.nodes') {
				cookie.filter = true;

				filter['filter.nodes[' + (nodeIndex++) + '].id'] = param.value;
				if(cookie['filter.nodes'] === undefined) {
					cookie['filter.nodes'] = [param.value];
				} else {
					cookie['filter.nodes'].push(param.value);
				}

				return;
			}
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
			if(param.value !== '') {
				cookie.filter = true;

				filter[param.name] = param.value;
				cookie[param.name] = param.value;

				return;
			}
		});

		Cookies.set('bsosFilter', cookie);

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

		Cookies.remove('bsosFilter');

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

		window.open(BASE_URL + 'exportBsoJournal.action?' + $.param(filter) + '&' + $.param(sorter));

		$('#exportBsoModal').modal('hide');
		bsosTableObj.bootstrapTable('refresh');
	});

	/**
	 * При изменении значения узла отправителя нужно менять значение узла получателя.
	 * Список возможных значений узла получателя и связи между значениями узла отправителя и узла получателя
	 * получаются при открытии акта и сохраняются через data.
	 * Можно было привязывать при открытии формы, но лучше это сделать отдельно, чтобы не тащить кучу всего
	 * через замыкание.
	 * Также очищаем ошибки при изменении полей.
	 */
	var actSenderInputObj = $('#actSenderInput');
	var actRecipientInputObj = $('#actRecipientInput');
	actSenderInputObj.on('change', function() {
		actSenderInputObj.parent().removeClass('has-error');
		actSenderInputObj.siblings('.with-errors').text('');
		actRecipientInputObj.parent().removeClass('has-error');
		actRecipientInputObj.siblings('.with-errors').text('');
		actRecipientInputObj.prop('disabled', true);
		actRecipientInputObj.find('.option').remove();

		var senderId = actSenderInputObj.val();
		if(senderId == 0) {
			return;
		}

		var links = actSenderInputObj.data('links');
		var itemsTo = actSenderInputObj.data('itemsTo');
		var itemFrom = actSenderInputObj.find(':selected').data('item');

		var nodeIds = [];
		$.each(links, function(index, link) {
			if(itemFrom.nodeIds.indexOf(link.key) > -1) {
				nodeIds.push(link.value);
			}
		});

		// Добавляем опции узла получателя с учётом их зависимости от выбранной опции узла отправителя.
		$.each(itemsTo, function(index, itemTo) {
			var skip = true;
			$.each(itemTo.nodeIds, function(index, nodeId) {
				if(nodeIds.indexOf(nodeId) > -1 && (itemFrom.cityId === 0 || itemTo.cityId === 0 || (itemFrom.cityId !== 0 && itemTo.cityId !== 0 && itemFrom.cityId === itemTo.cityId))) {
					skip = false;
					return false;
				}
			});
			if(skip) {
				return true;
			}

			$('<option class="option" value="' + itemTo.id + '">' + itemTo.name + '</option>')
				.data('item', itemTo)
				.appendTo(actRecipientInputObj);
		});
		// Если доступна только одна опция, то делаем её выбранной.
		if(actRecipientInputObj.find('.option').length == 1) {
			actRecipientInputObj.val(actRecipientInputObj.find('.option').val());
		} else {
			actRecipientInputObj.val(0);
		}


		actRecipientInputObj.prop('disabled', false);
	});
	actRecipientInputObj.on('change', function() {
		actRecipientInputObj.parent().removeClass('has-error');
		actRecipientInputObj.siblings('.with-errors').text('');
	});

	/**
	 * Очищаем ошибки добавления пачек документов при изменении соответствующих полей.
	 */
	$('#packSeriesInput, #packNumberFromInput, #packNumberToInput').on('keyup', function() {
		$('#packSeriesInput').parent().removeClass('has-error');
		$('#packNumberFromInput').parent().removeClass('has-error');
		$('#packNumberToInput').parent().removeClass('has-error');

		var numberFrom = parseInt($('#packNumberFromInput').val());
		var numberTo = parseInt($('#packNumberToInput').val());
		if(!isNaN(numberFrom) && !isNaN(numberTo) && numberTo - numberFrom + 1 > 0) {
			$('#packAmountInput').val(numberTo - numberFrom + 1);
		} else {
			$('#packAmountInput').val('');
		}
	});

	/**
	 * Очищаем ошибки выдачи документов страхователям при изменении соответствующих полей.
	 */
	var insuredObj = $('#actionInsuredInput');
	var premiumObj = $('#actionPremiumInput');
	insuredObj.on('keyup', function() {
		insuredObj.parent().removeClass('has-error');
		insuredObj.siblings('.with-errors').text('');
	});
	premiumObj.on('keyup', function() {
		premiumObj.parent().removeClass('has-error');
		premiumObj.siblings('.with-errors').text('');
	});
});
