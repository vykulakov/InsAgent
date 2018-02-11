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
 * @returns {Array} массив ошибок или undefined, если ошибок нет
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

$(function() {
});