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

package ru.insagent.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.insagent.management.model.CityFilter;
import ru.insagent.model.City;
import ru.insagent.model.User;

public class CityDao extends SimpleHDao<City> {
	{
		clazz = City.class;

		sortByMap.put("id", "c.id");
		sortByMap.put("name", "c.name");

		countQueryPrefix = ""
			+ " SELECT"
			+ "     COUNT(*) AS count"
			+ " FROM"
			+ "     City c"
			+ " WHERE"
			+ "     1 = 1";

		selectQueryPrefix = ""
			+ " SELECT"
			+ "     c"
			+ " FROM"
			+ "     City c"
			+ " WHERE"
			+ "     1 = 1";
	}

	public List<City> listByUser(User user) {
		return listByUser(user, null, null, 0, 0);
	}

	public List<City> listByUser(User user, String sortBy, String sortDir, int limitRows, int limitOffset) {
		return listByUser(user, (String) null, sortBy, sortDir, limitRows, limitOffset);
	}

	public List<City> listByUser(User user, String search, String sortBy, String sortDir, int limitRows, int limitOffset) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> objects = new HashMap<String, Object>();

		if(search != null && !search.trim().isEmpty()) {
			search = "%" + search + "%";

			sb.append("c.name LIKE :search");
			objects.put("search", search);
		}

		String where = null;
		if(objects.size() > 0) {
			where = sb.toString();
		} else {
			objects = null;
		}

		return listByWhere(where, objects, sortBy, sortDir, limitRows, limitOffset);
	}

	public List<City> listByUser(User user, CityFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> objects = new HashMap<String, Object>();

		if(filter != null) {
			sb.append("1 = 1");
			if(filter.getName() != null) {
				sb.append(" AND c.name LIKE :name");
				objects.put("name", "%" + filter.getName().replace("*", "%") + "%");
			}
		}

		String where = null;
		if(objects.size() > 0) {
			where = sb.toString();
		} else {
			objects = null;
		}

		return listByWhere(where, objects, sortBy, sortDir, limitRows, limitOffset);
	}
}
