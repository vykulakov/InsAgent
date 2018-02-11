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

package ru.insagent.management.user.action;

import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

import ru.insagent.action.BaseAction;
import ru.insagent.service.UserService;

public class RemoveUserJsonAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private Integer userId;
	public Integer getUserId() {
		return userId;
	}
	@RequiredFieldValidator(message = "Пользователь не передан.", shortCircuit = true)
	@ConversionErrorFieldValidator(message = "Невозможно преобразовать идентификатор пользователя.", shortCircuit = true)
	@IntRangeFieldValidator(message = "Идентификатор пользователя имеет недопустимое значение.", min = "1", shortCircuit = true)
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String executeImpl() {
		new UserService().remove(userId);

		return SUCCESS;
	}
}
