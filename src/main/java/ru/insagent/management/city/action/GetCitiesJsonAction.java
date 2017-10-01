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

import java.util.Arrays;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import ru.insagent.action.GetBaseAction;
import ru.insagent.management.model.CityFilter;
import ru.insagent.model.City;
import ru.insagent.service.CityService;

public class GetCitiesJsonAction extends GetBaseAction<City> {
	private static final long serialVersionUID = 1L;

	private CityFilter filter;
	public void setFilter(CityFilter filter) {
		this.filter = filter;
	}
	@JSON(serialize = false)
	public CityFilter getFilter() {
		return filter;
	}

	@Override
	public long getTotal() {
		return total;
	}

	@Override
	public List<City> getRows() {
		return rows;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin");
	}

	@Override
	public String executeImpl() {
		CityService cs = new CityService();

		if(filter == null) {
			rows = cs.listByUser(user, search, sort, order, limit, offset);
		} else {
			rows = cs.listByUser(user, filter, sort, order, limit, offset);
		}
		total = cs.getCount();

		return SUCCESS;
	}
}
