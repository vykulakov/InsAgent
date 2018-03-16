'use strict';

$.ajaxSetup({
    cache: false
});

/**
 * Проверяет ответ от сервера на наличие ошибок
 * @param {object} response - JSON ответ от сервера
 * @returns {*} true, если
 * @deprecated При обработке Ajax ответов используются HTTP коды ошибок, поэтому,
 * если был вызван error обработчик, то проверять уже нечего
 */
function checkError(response) {
    return undefined;
}

/**
 * Формирует элемент с сообщением об ошибке.
 *
 * @param {string} status - тип ошибки,
 * @param {Object} response - ответ сервера с ошибкой,
 * @param {string} response.error - тип ошибки,
 * @param {string} response.message - описание ошибки.
 * @return {string} Готовое для вставки в дерево сообщение об ошибке.
 */
function getAlertElements(status, response) {
    var elements = '';

    elements += '<div class="alert alert-danger fade in">';
    elements += '<a href="#" class="close" data-dismiss="alert">&times;</a>';
    switch (status) {
        case 'timeout':
            elements += '<strong>Ошибка выполнения запроса</strong>: сервер не отвечает';
            break;
        case 'error':
            elements += '<strong>Ошибка выполнения запроса</strong>:<br/>';
            elements += response.error + ': ' + response.message;
            break;
        case 'abort':
            elements += '<strong>Ошибка выполнения запроса</strong>: запрос отменён';
            break;
        case 'parsererror':
            elements += '<strong>Ошибка выполнения запроса</strong>: ошибка разбора ответа сервера';
            break;
        default:
            elements += '<strong>Ошибка выполнения запроса</strong>: данные не могут быть загружены';
    }
    elements += '</div>';

    return elements;
}

var csrfHeaders = {};
$(function () {
    var csrfToken = $('meta[name="_csrf"]').attr("content");
    var csrfHeader = $('meta[name="_csrf_header"]').attr("content");

    csrfHeaders[csrfHeader] = csrfToken;
});
