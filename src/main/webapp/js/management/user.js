'use strict';

var filter = {};

$(function () {
    var $table = $('#usersTable');
    var $alert = $('#usersTableAlert');
    var $filter = $('#filterUserForm');

    var $editForm = $('#editUserForm');
    var $editBody = $('#editUserBody');

    var $removeForm = $('#removeUserForm');
    var $removeBody = $('#removeUserBody');

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
            field: 'login',
            title: 'Логин',
            sortable: true
        }, {
            field: 'name',
            title: 'Имя',
            sortable: true
        }, {
            field: 'unitName',
            title: 'Подразделение',
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
        stateSaveIdTable: 'usersTable',

        onLoadError: function (status, response) {
            $alert.children().remove();
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
        } else {
            $('#editUserLabel').text('Редактирование пользователя');
            $('#editUserButton').text('Сохранить');

            $.ajax({
                url: '/management/user/' + id,
                type: 'GET',
                error: function (xhr) {
                    $editBody.prepend(getAlertElements(xhr.statusText, xhr.responseJSON));

                    $('#editUserModal').modal({});
                },
                success: function (response) {
                    $('#userIdInput').val(response.id);
                    $('#userUsernameInput').val(response.username);
                    $('#userFirstNameInput').val(response.firstName);
                    $('#userLastNameInput').val(response.lastName);
                    $('#userRoleInput').val(!!response.roles[0] ? response.roles[0].id : 0);
                    $('#userUnitInput').val(response.unit.id);
                    $('#userCommentInput').val(response.comment);

                    $('#editUserModal').modal({});
                }
            });
        }
    }

    $('#openAddUserButton').on('click', function () {
        openEdit()
    });

    $('#openEditUserButton').on('click', function () {
        var selections = $table.bootstrapTable('getSelections');

        if (selections.length < 1) {
            alert('Необходимо выбрать пользователя.');
            return;
        }
        if (selections.length > 1) {
            alert('Для редактирования необходимо выбрать только одного пользователя.');
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

        $('#removeUserModal').modal({});
    }

    $('#openRemoveUserButton').on('click', function () {
        var selections = $table.bootstrapTable('getSelections');

        if (selections.length < 1) {
            alert('Необходимо выбрать пользователя.');
            return;
        }
        if (selections.length > 1) {
            alert('Для удаления необходимо выбрать только одного пользователя.');
            return;
        }

        openRemove(selections[0].id);
    });

    $editForm.validator({
        custom: {
            password: function (element) {
                var target = element.data('password');
                return !(parseInt($(target).val()) === 0 && element.val().trim().length === 0);


            },
            passwordminlength: function (element) {
                var target = element.data('passwordminlength');
                return !(parseInt($(target).val()) === 0 && element.val().trim().length !== 0 && element.val().trim().length < 6);


            },
            passwordmatch: function (element) {
                var target = element.data('passwordmatch');
                return $(target).val() === element.val();
            },
            selected: function (element) {
                return element.val() != 0;


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
    }).on('submit', function (e) {
        if (e.isDefaultPrevented()) {
            return;
        }

        e.preventDefault();

        $editBody.find('.alert').remove();
        $.ajax({
            url: '/management/user',
            type: 'POST',
            data: $editForm.serialize(),
            headers: csrfHeaders,
            error: function (xhr) {
                $editBody.prepend(getAlertElements(xhr.statusText, xhr.responseJSON));
            },
            success: function (response) {
                $('#editUserModal').modal('hide');
                $table.bootstrapTable('refresh');
            }
        });
    });

    $removeForm.on('submit', function (e) {
        e.preventDefault();

        $removeBody.find('.alert').remove();
        $.ajax({
            url: '/management/user/' + $removeBody.data('id'),
            type: 'DELETE',
            headers: csrfHeaders,
            error: function (xhr) {
                $removeBody.prepend(getAlertElements(xhr.statusText, xhr.responseJSON));
            },
            success: function (response) {
                $('#removeUserModal').modal('hide');
                $table.bootstrapTable('refresh');
            }
        });
    });

    /**
     * Добавляем фильтр к таблице.
     */
    $table.parents('.bootstrap-table').find('.fixed-table-toolbar').append('' +
        '<div class="columns columns-left btn-group pull-right">' +
        '    <button id="openFilterUserButton" title="Фильтр пользователей" name="filter" type="button" class="btn btn-default">' +
        '        <i id="openFilterUserIcon" class="glyphicon glyphicon-menu-up icon-menu-up"></i>' +
        '    </button>' +
        '</div>');
    var $filterButton = $('#openFilterUserButton');
    $filterButton.on('click', function () {
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

    var cookie = Cookies.getJSON('usersFilter');
    if (!!cookie && cookie.filter) {
        filter = {};

        $filter.find(':input').each(function () {
            if (this.name && cookie[this.name]) {
                $(this).val(cookie[this.name]);
                if (this.name === 'unitIds') {
                    $(this).multiselect('refresh');
                }
            }
        });

        var unitIndex = 0;
        $filter.serializeArray().map(function (param) {
            if (param.name === 'unitIds') {
                filter['unitIds[' + (unitIndex++) + ']'] = param.value;
                return;
            }
            if (param.value !== '') {
                filter[param.name] = param.value;
                return;
            }
        });

        $filterButton.removeClass('btn-default').addClass('btn-warning');

        $table.bootstrapTable('refresh', {url: '/management/user'});
    } else {
        $table.bootstrapTable('refresh', {url: '/management/user'});
    }
    $filter.on('submit', function (e) {
        e.preventDefault();

        filter = {};

        var cookie = {};
        var unitIndex = 0;
        $filter.serializeArray().map(function (param) {
            if (param.name === 'unitIds') {
                cookie.filter = true;

                filter['unitIds[' + (unitIndex++) + ']'] = param.value;
                if (cookie['unitIds'] === undefined) {
                    cookie['unitIds'] = [param.value];
                } else {
                    cookie['unitIds'].push(param.value);
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

        Cookies.set('usersFilter', cookie);

        $('#filterUserDiv').toggleClass('hidden');
        $('#openFilterUserIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
        if (!!cookie && cookie.filter) {
            $filterButton.removeClass('btn-default').addClass('btn-warning');

            $table.bootstrapTable('refresh');
        }
    });
    $filter.on('reset', function (e) {
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
            $('#filterUserUnitsInput').multiselect('refresh');
            $('#filterUserRemovedInput').val('false');
        });

        Cookies.remove('usersFilter');

        $('#filterUserDiv').toggleClass('hidden');
        $('#openFilterUserIcon').toggleClass('glyphicon-menu-up glyphicon-menu-down');
        $filterButton.removeClass('btn-warning').addClass('btn-default');

        $table.bootstrapTable('refresh');
    });
});
