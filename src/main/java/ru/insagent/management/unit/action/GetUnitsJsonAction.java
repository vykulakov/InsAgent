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

package ru.insagent.management.unit.action;

import org.apache.struts2.json.annotations.JSON;
import ru.insagent.action.GetBaseAction;
import ru.insagent.management.model.UnitFilter;
import ru.insagent.model.Unit;
import ru.insagent.service.UnitService;

import java.util.Arrays;
import java.util.List;

public class GetUnitsJsonAction extends GetBaseAction<Unit> {
	private static final long serialVersionUID = -4724835910757366392L;

	private UnitFilter filter;

	public void setFilter(UnitFilter filter) {
		this.filter = filter;
	}

	@JSON(serialize = false)
	public UnitFilter getFilter() {
		return filter;
	}

	@Override
	public List<Unit> getRows() {
		return rows;
	}

	@Override
	public long getTotal() {
		return total;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin");
		ALLOW_MSG = "У вас нет прав для просмотра списка подразделений";
	}

	@Override
	public String executeImpl() {
		UnitService us = new UnitService();

		if (filter == null) {
			rows = us.listByUser(baseUser, search, sort, order, limit, offset);
		} else {
			rows = us.listByUser(baseUser, filter, sort, order, limit, offset);
		}
		total = us.getCount();

		return SUCCESS;
	}
}
