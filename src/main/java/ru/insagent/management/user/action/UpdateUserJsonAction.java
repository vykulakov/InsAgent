/*
 * InsAgent - https://github.com/vykulakov/InsAgent
 *
 * Copyright 2018 Vyacheslav Kulakov
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

import com.opensymphony.xwork2.validator.annotations.*;
import ru.insagent.action.BaseAction;
import ru.insagent.model.User;
import ru.insagent.service.UserService;

public class UpdateUserJsonAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private User user;

	public User getUser() {
		return user;
	}

	@RequiredFieldValidator(message = "Пользователь не передан.", shortCircuit = true)
	public void setUser(User user) {
		this.user = user;
	}

	private String confirm;

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	@Override
	@Validations(
			requiredFields = {
					@RequiredFieldValidator(fieldName = "user.id", message = "Идентификатор пользователя не указан.", shortCircuit = true),
					@RequiredFieldValidator(fieldName = "user.unit.id", message = "Идентификатор подразделения не указан.", shortCircuit = true),
					@RequiredFieldValidator(fieldName = "user.roles", message = "Роль пользователя не найдена.", shortCircuit = true)
			},
			requiredStrings = {
					@RequiredStringValidator(fieldName = "user.username", message = "Логин пользователя не указан.", shortCircuit = true),
					@RequiredStringValidator(fieldName = "user.firstName", message = "Имя пользователя не указано.", shortCircuit = true),
					@RequiredStringValidator(fieldName = "user.lastName", message = "Фамилия пользователя не указана.", shortCircuit = true),
			},
			conversionErrorFields = {
					@ConversionErrorFieldValidator(fieldName = "user.id", message = "Невозможно преобразовать идентификатор пользователя.", shortCircuit = true),
					@ConversionErrorFieldValidator(fieldName = "user.unit.id", message = "Невозможно преобразовать идентификатор подразделения.", shortCircuit = true)
			},
			intRangeFields = {
					@IntRangeFieldValidator(fieldName = "user.id", min = "0", message = "Идентификатор пользователя имеет недопустимое значение.", shortCircuit = true),
					@IntRangeFieldValidator(fieldName = "user.unit.id", min = "0", max = "255", message = "Идентификатор подразделения имеет недопустимое значение.", shortCircuit = true)
			},
			stringLengthFields = {
					@StringLengthFieldValidator(fieldName = "user.username", maxLength = "255", message = "Логин пользователя должен содержать менее 255 символов."),
					@StringLengthFieldValidator(fieldName = "user.firstName", maxLength = "255", message = "Имя пользователя должно содержать менее 255 символов."),
					@StringLengthFieldValidator(fieldName = "user.lastName", maxLength = "255", message = "Фамилия пользователя должна содержать менее 255 символов."),
					@StringLengthFieldValidator(fieldName = "user.comment", maxLength = "2048", message = "Комментарий к пользователю должен содержать менее 2048 символов."),
			}
	)
	public String executeImpl() {
		new UserService().update(user);

		return SUCCESS;
	}

	@Override
	public void validate() {
		if (user != null && user.getId() == 0 && (user.getPassword() == null || user.getPassword().isEmpty())) {
			addFieldError("user.password", "Пароль пользователя не указан.");
			return;
		}
		if (user != null && user.getPassword() != null && !user.getPassword().isEmpty() && user.getPassword().length() < 6) {
			addFieldError("user.password", "Пароль слишком короткий.");
			return;
		}
		if (user != null && user.getPassword() != null && !user.getPassword().isEmpty() && (confirm == null || !confirm.equals(user.getPassword()))) {
			addFieldError("confirm", "Введённые пароли не совпадают.");
			return;
		}
	}
}
