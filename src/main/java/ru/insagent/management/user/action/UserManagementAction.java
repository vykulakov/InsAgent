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

import java.util.ArrayList;
import java.util.List;

import ru.insagent.action.BaseAction;
import ru.insagent.model.Role;
import ru.insagent.model.Unit;
import ru.insagent.service.UserService;

public class UserManagementAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private List<Role> roles = new ArrayList<Role>();
	public List<Role> getRoles() {
		return roles;
	}

	private List<Unit> units = new ArrayList<Unit>();
	public List<Unit> getUnits() {
		return units;
	}

	@Override
	public String executeImpl() {
		UserService us = new UserService();

		roles = us.roles();
		units = us.units();

		return SUCCESS;
	}
}
