'use strict';

$(function() {
	$('#actsTable').bootstrapTable({
		url: 'listActsJson.action',
		responseHandler: function(response) {
			$('#actsTableAlert').children().remove();

			var errors = checkError(response);
			if(errors !== undefined) {
				var alertMsg = '' +
				'<div id="actsTableAlert" class="alert alert-danger fade in">' +
				'    <a href="#" class="close" data-dismiss="alert">&times;</a>' +
				'    <strong>При получении списка актов возникли ошибки:</strong><br/>';
				for(var i = 0, l = errors.length; i < l; i++) {
					alertMsg += errors[i] + '<br/>';
				}
				alertMsg += '</div>';

				$('#actsTableAlert').append(alertMsg);

				return [];
			}

			return response;
		},
		clickToSelect: false,
		singleSelect: false,
		checkboxHeader: false,
		columns: [{
			field: 'created',
			title: 'Создан',
			sortable: true
		}, {
			field: 'type',
			title: 'Тип',
			sortable: true,
			formatter: function(type) {
				return type.fullName;
			}
		}, {
			field: 'nodeFrom',
			title: 'Из узла',
			sortable: true,
			formatter: function(nodeFrom) {
				return nodeFrom.name;
			}
		}, {
			field: 'nodeTo',
			title: 'В узел',
			sortable: true,
			formatter: function(node) {
				return node.name;
			}
		}, {
			field: 'unitFrom',
			title: 'Из подразделения',
			sortable: true,
			formatter: function(unitFrom) {
				if(unitFrom == null) {
					return null;
				} else {
					return unitFrom.name;
				}
			}
		}, {
			field: 'unitTo',
			title: 'В подразделение',
			sortable: true,
			formatter: function(unitTo) {
				if(unitTo == null) {
					return null;
				} else {
					return unitTo.name;
				}
			}
		}, {
			field: 'amount',
			title: 'Количество',
			sortable: true
		}],
		detailView: true,
		onExpandRow: function(index, row, $detail) {
			$detail.before('<td>&nbsp;</td>');
			$detail.attr('colspan', '7');
			$('<table/>')
				.bootstrapTable({
					data: row.packs,
					clickToSelect: false,
					singleSelect: false,
					checkboxHeader: false,
					columns: [{
						field: 'series',
						title: 'Серия'
					}, {
						field: 'numberFrom',
						title: 'Номер с'
					}, {
						field: 'numberTo',
						title: 'Номер по'
					}, {
						field: 'amount',
						title: 'Количество'
					}]
				})
				.appendTo($detail);
		},
		search: true,
		showRefresh: true,
		pagination: true,
		sidePagination: 'server',
		pageSize: 20,
		pageList: [10, 20, 50, 100]
	});
});
