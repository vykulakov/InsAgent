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

import org.apache.struts2.json.annotations.JSON;
import ru.insagent.action.GetBaseAction;
import ru.insagent.management.model.UserFilter;
import ru.insagent.model.User;
import ru.insagent.service.UserService;

import java.util.List;

public class GetUsersJsonAction extends GetBaseAction<User> {
	private static final long serialVersionUID = 1L;

	private UserFilter filter;

	public void setFilter(UserFilter filter) {
		this.filter = filter;
	}

	@JSON(serialize = false)
	public UserFilter getFilter() {
		return filter;
	}

	@Override
	public List<User> getRows() {
		return rows;
	}

	@Override
	public long getTotal() {
		return total;
	}

	@Override
	public String executeImpl() {
		UserService us = new UserService();

		if (filter == null) {
			rows = us.listByUser(baseUser, search, sort, order, limit, offset);
		} else {
			rows = us.listByUser(baseUser, filter, sort, order, limit, offset);
		}
		total = us.getCount();

		return SUCCESS;
	}
}
