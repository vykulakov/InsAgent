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

package ru.insagent.management.unit.action;

import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

import ru.insagent.action.BaseAction;
import ru.insagent.model.Unit;
import ru.insagent.service.UnitService;

public class UpdateUnitJsonAction extends BaseAction {
	private static final long serialVersionUID = 4911618257796238681L;

	private Unit unit;
	public Unit getUnit() {
		return unit;
	}
	@RequiredFieldValidator(message = "Подразделение не передано.", shortCircuit = true)
	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@Override
	@Validations(
		requiredFields = {
			@RequiredFieldValidator(fieldName = "unit.id",      message = "Идентификатор подразделения не указан.", shortCircuit = true),
			@RequiredFieldValidator(fieldName = "unit.type.id", message = "Идентификатор типа не указан.", shortCircuit = true),
			@RequiredFieldValidator(fieldName = "unit.city.id", message = "Идентификатор города не указан.", shortCircuit = true)
		},
		requiredStrings = {
			@RequiredStringValidator(fieldName = "unit.name",  message = "Название подразделения не указано.", shortCircuit = true)
		},
		conversionErrorFields = {
			@ConversionErrorFieldValidator(fieldName = "unit.id",      message = "Невозможно преобразовать идентификатор подразделения.", shortCircuit = true),
			@ConversionErrorFieldValidator(fieldName = "unit.type.id", message = "Невозможно преобразовать идентификатор типа.", shortCircuit = true),
			@ConversionErrorFieldValidator(fieldName = "unit.city.id", message = "Невозможно преобразовать идентификатор города.", shortCircuit = true)
		},
		intRangeFields = {
			@IntRangeFieldValidator(fieldName = "unit.id",      min = "0", max = "255", message = "Идентификатор подразделения имеет недопустимое значение.", shortCircuit = true),
			@IntRangeFieldValidator(fieldName = "unit.type.id", min = "0", max = "255", message = "Идентификатор типа имеет недопустимое значение.", shortCircuit = true),
			@IntRangeFieldValidator(fieldName = "unit.city.id", min = "0", max = "255", message = "Идентификатор города имеет недопустимое значение.", shortCircuit = true)
		},
		stringLengthFields = {
			@StringLengthFieldValidator(fieldName = "unit.name",    maxLength = "255",  message = "Название подразделения должно содержать менее 255 символов."),
			@StringLengthFieldValidator(fieldName = "unit.comment", maxLength = "2048", message = "Комментарий к подразделению должен содержать менее 2048 символов.")
		}
		)
	public String executeImpl() {
		new UnitService().update(unit);

		return SUCCESS;
	}
}
