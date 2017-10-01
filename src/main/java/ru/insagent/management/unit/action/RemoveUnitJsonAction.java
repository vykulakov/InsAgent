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

import ru.insagent.action.BaseAction;
import ru.insagent.service.UnitService;

public class RemoveUnitJsonAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private Integer unitId;
	public Integer getUnitId() {
		return unitId;
	}
	@RequiredFieldValidator(message = "Подразделение не передано.", shortCircuit = true)
	@ConversionErrorFieldValidator(message = "Невозможно преобразовать идентификатор подразделения.", shortCircuit = true)
	@IntRangeFieldValidator(message = "Идентификатор подразделения имеет недопустимое значение.", min = "1", shortCircuit = true)
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	@Override
	public String executeImpl() {
		new UnitService().remove(unitId);

		return SUCCESS;
	}
}
