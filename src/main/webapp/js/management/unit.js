'use strict';

var filter = {};

$(function () {
    var $table = $('#unitsTable');
    var $alert = $('#unitsTableAlert');
    var $filter = $('#filterUnitForm');

    var $editForm = $('#editUnitForm');
    var $editBody = $('#editUnitBody');

    var $removeForm = $('#removeUnitForm');
    var $removeBody = $('#removeUnitBody');

    $table.bootstrapTable({
        url: '',
        queryParams: function (params) {
            $.extend(params, filter);

            return params;
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
        rowStyle: function (row) {
            if (row.removed) {
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
        stateSaveIdTable: 'unitsTable',

        onLoadError: function (status, response) {
            $alert.prepend(getAlertElements(status, response));
        },
        onLoadSuccess: function () {
            $alert.children().remove();
        },
        onDblClickRow: function (row) {
            openEdit(row.id);
        }
    });

    /**
     * Загружает сущность по идентификатору с сервера, заполняет форму параметрами загруженной сущности и
     * открывает модальное окно для редактирования выбранной сущности.
     *
     * @param {number} [id] - идентификатор сущности для редактирования.
     */
    function openEdit(id) {
        $editBody.find('.alert').remove();

        if (!id) {
            $('#unitIdInput').val(0);
            $('#unitNameInput').val('');
            $('#unitTypeInput').val(0);
            $('#unitCityInput').val(0);
            $('#unitCommentInput').val('');

            $('#editUnitLabel').text('Добавление подразделения');
            $('#editUnitButton').text('Добавить');

            $('#editUnitModal').modal({});
        } else {
            $('#editUnitLabel').text('Редактирование подразделения');
            $('#editUnitButton').text('Сохранить');

            $.ajax({
                url: '/management/unit/' + id,
                type: 'GET',
                error: function (xhr) {
                    $editBody.prepend(getAlertElements(xhr.statusText, xhr.responseJSON));

                    $('#editUnitModal').modal({});
                },
                success: function (response) {
                    $('#unitIdInput').val(response.id);
                    $('#unitNameInput').val(response.name);
                    $('#unitTypeInput').val(response.type.id);
                    $('#unitCityInput').val(response.city.id);
                    $('#unitCommentInput').val(response.comment);

                    $('#editUnitModal').modal({});
                }
            });
        }
    }

    $('#openAddUnitButton').on('click', function () {
        openEdit();
    });

    $('#openEditUnitButton').on('click', function () {
        var selections = $table.bootstrapTable('getSelections');

        if (selections.length < 1) {
            alert('Необходимо выбрать подразделение.');
            return;
        }
        if (selections.length > 1) {
            alert('Для редактирования необходимо выбрать только одно подразделение.');
            return;
        }

        openEdit(selections[0].id);
    });

    /**
     * Открывает диалог подтверждения удаления сущности.
     *
     * @param {number} id - идентификатор сущности для удаления.
     */
    function openRemove(id) {
        $removeBody.data('id', id);
        $removeBody.find('.alert').remove();

        $('#removeUnitModal').modal({});
    }

    $('#openRemoveUnitButton').on('click', function () {
        var selections = $table.bootstrapTable('getSelections');

        if (selections.length < 1) {
            alert('Необходимо выбрать подразделение.');
            return;
        }
        if (selections.length > 1) {
            alert('Для удаления необходимо выбрать только одно подразделение.');
            return;
        }

        openRemove(selections[0].id);
    });

    $editForm.validator({
        custom: {
            selected: function (element) {
                return element.val() !== 0;

            }
        },
        errors: {
            match: "Значения полей не совпадают",
            minlength: "Значение слишком короткое",
            selected: "Значение не выбрано"
        }
    }).on('submit', function (e) {
        if (e.isDefaultPrevented()) {
            return;
        }

        e.preventDefault();

        $editBody.find('.alert').remove();
        $.ajax({
            url: '/management/unit',
            type: 'POST',
            data: $editForm.serialize(),
            headers: csrfHeaders,
            error: function (xhr) {
                $editBody.prepend(getAlertElements(xhr.statusText, xhr.responseJSON));
            },
            success: function (response) {
                $('#editUnitModal').modal('hide');
                $table.bootstrapTable('refresh');
            }
        });
    });

    $removeForm.on('submit', function (e) {
        e.preventDefault();

        $removeBody.find('.alert').remove();
        $.ajax({
            url: '/management/unit/' + $removeBody.data('id'),
            type: 'DELETE',
            headers: csrfHeaders,
            error: function (xhr) {
                $removeBody.prepend(getAlertElements(xhr.statusText, xhr.responseJSON));
            },
            success: function (response) {
                $('#removeUnitModal').modal('hide');
                $table.bootstrapTable('refresh');
            }
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

    var $filterButton = $('#openFilterUnitButton');
    $filterButton.on('click', function () {
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

    var cookie = Cookies.getJSON('unitsFilter');
    if (!!cookie && cookie.filter) {
        filter = {};

        $filter.find(':input').each(function () {
            if (this.name && cookie[this.name]) {
                $(this).val(cookie[this.name]);
                if (this.name === 'types' || this.name === 'cities') {
                    $(this).multiselect('refresh');
                }
            }
        });

        var typeIndex = 0;
        var cityIndex = 0;
        $filter.serializeArray().map(function (param) {
            if (param.name === 'types') {
                filter['types[' + (typeIndex++) + '].id'] = param.value;
                return;
            }
            if (param.name === 'cities') {
                filter['cities[' + (cityIndex++) + '].id'] = param.value;
                return;
            }
            if (param.value !== '') {
                filter[param.name] = param.value;
                return;
            }
        });

        $filterButton.removeClass('btn-default').addClass('btn-warning');

        $table.bootstrapTable('refresh', {url: '/management/unit'});
    } else {
        $table.bootstrapTable('refresh', {url: '/management/unit'});
    }
    $filter.on('submit', function (e) {
        e.preventDefault();

        filter = {};

        var cookie = {};
        var typeIndex = 0;
        var cityIndex = 0;
        $filter.serializeArray().map(function (param) {
            if (param.name === 'types') {
                cookie.filter = true;

                filter['types[' + (typeIndex++) + '].id'] = param.value;
                if (cookie['types'] === undefined) {
                    cookie['types'] = [param.value];
                } else {
                    cookie['types'].push(param.value);
                }

                return;
            }
            if (param.name === 'cities') {
                cookie.filter = true;

                filter['cities[' + (cityIndex++) + '].id'] = param.value;
                if (cookie['cities'] === undefined) {
                    cookie['cities'] = [param.value];
                } else {
                    cookie['cities'].push(param.value);
                }

                return;
            }
            if (param.name === 'removed' && param.value === 'true') {
                cookie.filter = true;

                filter[param.name] = param.value;
                cookie[param.name] = param.value;

                return;
            }
            if (param.value !== '') {
                cookie.filter = true;

                filter[param.name] = param.value;
                cookie[param.name] = param.value;

                return;
            }
        });

        Cookies.set('unitsFilter', cookie);

        $('#filterUnitDiv').toggleClass('hidden');
        $('#openFilterUnitIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
        if (!!cookie && cookie.filter) {
            $filterButton.removeClass('btn-default').addClass('btn-warning');

            $table.bootstrapTable('refresh');
        }
    });
    $filter.on('reset', function (e) {
        // Форму отправлять не нужно.
        e.preventDefault();

        filter = {};
        $filter.find(':input').each(function () {
            switch (this.type) {
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
        $filterButton.removeClass('btn-warning').addClass('btn-default');

        $table.bootstrapTable('refresh');
    });
});
