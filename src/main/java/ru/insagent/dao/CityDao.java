/*
 * InsAgent - https://github.com/vykulakov/InsAgent
 *
 * Copyright 2017-2018 Vyacheslav Kulakov
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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.insagent.management.city.model.CityFilter;
import ru.insagent.model.City;
import ru.insagent.model.User;

@Repository
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

	public List<City> listByUser(User user, CityFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> objects = new HashMap<>();

		if(filter != null) {
			sb.append("1 = 1");
			if(StringUtils.isNotBlank(filter.getName())) {
				sb.append(" AND c.name LIKE :name");
				objects.put("name", "%" + filter.getName().replace("*", "%") + "%");
			}
			if(StringUtils.isNotBlank(filter.getSearch())) {
				sb.append(" AND c.name LIKE :search");
				objects.put("search", "%" + filter.getSearch() + "%");
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
