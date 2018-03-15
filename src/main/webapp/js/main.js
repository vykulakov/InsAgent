'use strict';

$.ajaxSetup({
	cache: false,
	error: function(xhr, status, error) {
		var alertMsg = '' +
			'Ошибка выполнения запроса к серверу:\n' +
			'    Статус: ' + status + '\n' +
			'    Ошибка: ' + error;
		alert(alertMsg);
	}
});

/**
 * Проверяет ответ от сервера на наличие ошибок
 * @param {object} response - JSON ответ от сервера
 * @returns {boolean} true, если
 */
function checkError(response) {
	var n = 0;
	var errors = [];

	if(response.actionErrors !== undefined) {
		for(var i = 0, l = response.actionErrors.length; i < l; i++) {
			errors[n++] = response.actionErrors[i];
		}
	}

	if(response.fieldErrors !== undefined) {
		for(var key in response.fieldErrors) {
			for(var i = 0, l = response.fieldErrors[key].length; i < l; i++) {
				errors[n++] = response.fieldErrors[key][i];
			}
		}
	}

	if(response.exception !== undefined) {
		errors[n++] = '<pre>';
		errors[n++] = response.exceptionStack;
		errors[n++] = '</pre>';
	}

	if(errors.length > 0) {
		return errors;
	} else {
		return undefined;
	}
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
    switch(status) {
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