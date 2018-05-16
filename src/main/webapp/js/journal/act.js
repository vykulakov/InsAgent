'use strict';

$(function () {
    var $table = $('#actsTable');
    var $alert = $('#actsTableAlert');

    $table.bootstrapTable({
        url: '/act',
        clickToSelect: false,
        singleSelect: false,
        checkboxHeader: false,
        columns: [{
            field: 'created',
            title: 'Создан',
            sortable: true
        }, {
            field: 'typeName',
            title: 'Тип',
            sortable: true
        }, {
            field: 'nodeFromName',
            title: 'Из узла',
            sortable: true
        }, {
            field: 'nodeToName',
            title: 'В узел',
            sortable: true
        }, {
            field: 'unitFromName',
            title: 'Из подразделения',
            sortable: true
        }, {
            field: 'unitToName',
            title: 'В подразделение',
            sortable: true
        }, {
            field: 'amount',
            title: 'Количество',
            sortable: true
        }],
        detailView: true,
        onExpandRow: function (index, row, $detail) {
            $detail.before('<td>&nbsp;</td>');
            $detail.attr('colspan', '7');
            $('<table/>').bootstrapTable({
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
            }).appendTo($detail);
        },
        search: true,
        showRefresh: true,
        pagination: true,
        sidePagination: 'server',
        pageSize: 20,
        pageList: [10, 20, 50, 100],
        stateSave: true,
        stateSaveIdTable: 'actsTable',

        onLoadError: function (status, response) {
            $alert.children().remove();
            $alert.prepend(getAlertElements(status, response));
        },
        onLoadSuccess: function () {
            $alert.children().remove();
        }
    });
});
