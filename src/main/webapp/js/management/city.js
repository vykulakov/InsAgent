'use strict';

var filter = {};

$(function () {
    var $table = $('#citiesTable');
    var $alert = $('#citiesTableAlert');
    var $filter = $('#filterCityForm');

    var $editForm = $('#editCityForm');
    var $editBody = $('#editCityBody');

    $table.bootstrapTable({
        url: '',
        queryParams: function (params) {
            $.extend(params, filter);

            return params;
        },
        responseHandler: function (response) {

            var errors = checkError(response);
            if (errors !== undefined) {
                var alertMsg = '' +
                    '<div class="alert alert-danger fade in">' +
                    '    <a href="#" class="close" data-dismiss="alert">&times;</a>' +
                    '    <strong>При получении данных возникли ошибки:</strong><br/>';
                for (var i = 0, l = errors.length; i < l; i++) {
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
        }],
        /**
         *
         * @param row
         * @param {boolean} row.removed
         * @returns {*}
         */
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
        stateSaveIdTable: 'citiesTable',

        onLoadError: function (status, response) {
            $alert.prepend(getAlertElements(status, response));
        },
        onLoadSuccess: function () {
            $alert.children().remove();
        },
        onDblClickRow: function (row) {
            openEditCity(row.id);
        }
    });

    $('#openAddCityButton').on('click', function () {
        openEditCity();
    });

    /**
     * Загружает сущность по идентификатору с сервера, заполняет форму параметрами загруженной сущности и
     * открывает модальное окно для редактирования выбранной сущности.
     *
     * @param {number} [id] - идентификатор сущности для редактирования.
     */
    function openEditCity(id) {
        $editBody.find('.alert').remove();

        if (!id) {
            $('#cityIdInput').val(0);
            $('#cityNameInput').val('');
            $('#cityCommentInput').val('');

            $('#editCityLabel').text('Добавление города');
            $('#editCityButton').text('Добавить');

            $('#editCityModal').modal({});
        } else {
            $('#editCityLabel').text('Редактирование города');
            $('#editCityButton').text('Сохранить');

            $.ajax({
                url: '/management/city/' + id,
                type: 'GET',
                error: function (xhr) {
                    $editBody.prepend(getAlertElements(xhr.statusText, xhr.responseJSON));

                    $('#editCityModal').modal({});
                },
                success: function (response) {
                    $('#cityIdInput').val(response.id);
                    $('#cityNameInput').val(response.name);
                    $('#cityCommentInput').val(response.comment);

                    $('#editCityModal').modal({});
                }
            });
        }
    }

    $('#openEditCityButton').on('click', function () {
        var selections = $table.bootstrapTable('getSelections');

        if (selections.length < 1) {
            alert('Необходимо выбрать город.');
            return;
        }
        if (selections.length > 1) {
            alert('Для редактирования необходимо выбрать только один город.');
            return;
        }

        openEditCity(selections[0].id);
    });

    /**
     * Открывает диалог подтверждения удаления сущности.
     *
     * @param {number} id - идентификатор сущности для удаления.
     */
    function openRemoveCity(id) {
        $('#removeCityBody').find('.alert').remove();

        $('#removeCityIdInput').val(id);

        $('#removeCityModal').modal({});
    }

    $('#openRemoveCityButton').on('click', function () {
        var selections = $('#citiesTable').bootstrapTable('getSelections');

        if (selections.length < 1) {
            alert('Необходимо выбрать город.');
            return;
        }
        if (selections.length > 1) {
            alert('Для удаления необходимо выбрать только один город.');
            return;
        }

        openEditCity(selections[0].id);
    });

    $editForm.validator({
        errors: {
            match: "Значения полей не совпадают",
            minlength: "Значение слишком короткое"
        }
    }).on('submit', function (e) {
        if (e.isDefaultPrevented()) {
            return;
        }

        e.preventDefault();

        $editBody.find('.alert').remove();
        $.ajax({
            url: '/management/city',
            type: 'POST',
            data: $editForm.serialize(),
            headers: csrfHeaders,
            error: function (xhr) {
                $editBody.prepend(getAlertElements(xhr.statusText, xhr.responseJSON));
            },
            success: function (response) {
                $('#editCityModal').modal('hide');
                $table.bootstrapTable('refresh');
            }
        });
    });

    var $removeForm = $('#removeCityForm');
    var $removeBody = $('#removeCityBody');
    $removeForm.on('submit', function (e) {
        e.preventDefault();

        $removeBody.find('.alert').remove();
        $.ajax({
            url: '/management/city/' + $removeForm.serialize().id,
            type: 'DELETE',
            headers: csrfHeaders,
            error: function (xhr) {
                $removeBody.prepend(getAlertElements(xhr.statusText, xhr.responseJSON));
            },
            success: function (response) {
                $('#removeCityModal').modal('hide');
                $table.bootstrapTable('refresh');
            }
        });
    });

    /**
     * Добавляем фильтр к таблице.
     */
    $table.parents('.bootstrap-table').find('.fixed-table-toolbar').append('' +
        '<div class="columns columns-left btn-group pull-right">' +
        '    <button id="openFilterCityButton" title="Фильтр городов" name="filter" type="button" class="btn btn-default">' +
        '        <i id="openFilterCityIcon" class="glyphicon glyphicon-menu-up icon-menu-up"></i>' +
        '    </button>' +
        '</div>');

    var $filterButton = $('#openFilterCityButton');
    $filterButton.on('click', function () {
        $('#filterCityDiv').toggleClass('hidden');
        $('#openFilterCityIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
    });

    var cookie = Cookies.getJSON('citiesFilter');
    if (!!cookie && cookie.filter) {
        filter = {};

        $filter.find(':input').each(function () {
            if (this.name && cookie[this.name]) {
                $(this).val(cookie[this.name]);
            }
        });

        $filter.serializeArray().map(function (param) {
            if (param.value !== '') {
                filter[param.name] = param.value;
            }
        });

        $filterButton.removeClass('btn-default').addClass('btn-warning');

        $table.bootstrapTable('refresh', {url: '/management/cities'});
    } else {
        $table.bootstrapTable('refresh', {url: '/management/cities'});
    }
    $filter.on('submit', function (e) {
        e.preventDefault();

        filter = {};

        var cookie = {};
        $filter.serializeArray().map(function (param) {
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

        Cookies.set('citiesFilter', cookie);

        $('#filterCityDiv').toggleClass('hidden');
        $('#openFilterCityIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
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
            $('#filterCityRemovedInput').val('false');
        });

        Cookies.remove('citiesFilter');

        $('#filterCityDiv').toggleClass('hidden');
        $('#openFilterCityIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
        $filterButton.removeClass('btn-warning').addClass('btn-default');

        $table.bootstrapTable('refresh');
    });
});
