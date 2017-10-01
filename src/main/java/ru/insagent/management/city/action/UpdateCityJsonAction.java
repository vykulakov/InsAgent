/*
 * InsAgent - https://github.com/vykulakov/InsAgent
 *
 * Copyright 2017 Vyacheslav Kulakov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.insagent.management.city.action;

import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

import ru.insagent.action.BaseAction;
import ru.insagent.model.City;
import ru.insagent.service.CityService;

public class UpdateCityJsonAction extends BaseAction {
	private static final long serialVersionUID = -7830209583064487692L;

	private City city;
	public City getCity() {
		return city;
	}
	@RequiredFieldValidator(message = "Город не передан.", shortCircuit = true)
	public void setCity(City city) {
		this.city = city;
	}

	@Override
	@Validations(
		requiredFields = {
			@RequiredFieldValidator(fieldName = "city.id", message = "Идентификатор города не указан.", shortCircuit = true)
		},
		requiredStrings = {
			@RequiredStringValidator(fieldName = "city.name", message = "Название города не указано.", shortCircuit = true)
		},
		conversionErrorFields = {
			@ConversionErrorFieldValidator(fieldName = "city.id", message = "Невозможно преобразовать идентификатор города.", shortCircuit = true)
		},
		intRangeFields = {
			@IntRangeFieldValidator(fieldName = "city.id", min = "0", max = "255", message = "Идентификатор города имеет недопустимое значение.", shortCircuit = true),
		},
		stringLengthFields = {
			@StringLengthFieldValidator(fieldName = "city.name",    maxLength = "255",  message = "Название города должно содержать менее 255 символов."),
			@StringLengthFieldValidator(fieldName = "city.comment", maxLength = "2048", message = "Комментарий к городу должен содержать менее 2048 символов.")
		}
		)
	public String executeImpl() {
		new CityService().update(city);

		return SUCCESS;
	}
}
