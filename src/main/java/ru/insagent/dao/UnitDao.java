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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.insagent.management.model.UnitFilter;
import ru.insagent.management.model.UnitType;
import ru.insagent.model.City;
import ru.insagent.model.Unit;
import ru.insagent.model.User;

public class UnitDao extends SimpleHDao<Unit> {
	{
		clazz = Unit.class;

		sortByMap.put("id", "u.id");
		sortByMap.put("name", "u.name");
		sortByMap.put("type", "t.name");
		sortByMap.put("city", "c.name");

		countQueryPrefix = ""
			+ " SELECT"
			+ "     COUNT(*) AS count"
			+ " FROM"
			+ "     Unit u"
			+ "     LEFT JOIN u.type t,"
			+ "     LEFT JOIN u.city c"
			+ " WHERE"
			+ "     1 = 1";

		selectQueryPrefix = ""
			+ " SELECT"
			+ "     u"
			+ " FROM"
			+ "     Unit u"
			+ "     LEFT JOIN u.type t,"
			+ "     LEFT JOIN u.city c"
			+ " WHERE"
			+ "     1 = 1";

		searchWhere = ""
			+ "     u.name LIKE :search OR"
			+ "     t.name LIKE :search OR"
			+ "     c.name LIKE :search";
	}

	/**
	 * Идентификатор типа подразделения "Центральный офис".
	 */
	public static final int CENTRAL_TYPE_ID = 1;

	/**
	 * Идентификатор типа подразделения "Центральный офис филиала".
	 */
	public static final int FILIAL_TYPE_ID = 2;

	/**
	 * Идентификатор типа подразделения "Точка продаж".
	 */
	public static final int POINT_TYPE_ID = 3;

	public List<Unit> listByUser(User user) {
		return listByUser(user, null, null, 0, 0);
	}

	public List<Unit> listByUser(User user, String sortBy, String sortDir, int limitRows, int limitOffset) {
		return listByUser(user, (String) null, sortBy, sortDir, limitRows, limitOffset);
	}

	public List<Unit> listByUser(User user, String search, String sortBy, String sortDir, int limitRows, int limitOffset) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> objects = new HashMap<String, Object>();

		if(search != null && !search.trim().isEmpty()) {
			search = "%" + search + "%";

			sb.append("u.name LIKE :search OR");
			sb.append("t.name LIKE :search OR");
			sb.append("c.name LIKE :search OR");
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

	public List<Unit> listByUser(User user, UnitFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> objects = new HashMap<String, Object>();

		if(filter != null) {
			sb.append("1 = 1");
			if(filter.getName() != null) {
				sb.append(" AND u.name LIKE :name");
				objects.put("name", "%" + filter.getName().replace("*", "%") + "%");
			}
			if(filter.getTypes() != null && !filter.getTypes().isEmpty()) {
				sb.append(" AND t.id IN :typeIds");
				objects.put("typeIds", filter.getTypes().stream().map(UnitType::getId).collect(Collectors.toList()));
			}
			if(filter.getCities() != null && !filter.getCities().isEmpty()) {
				sb.append(" AND c.id IN :cityIds");
				objects.put("cityIds", filter.getCities().stream().map(City::getId).collect(Collectors.toList()));
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

	public List<Unit> listByTypeIds(Collection<Integer> typeIds) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> objects = new HashMap<String, Object>();

		if(typeIds != null && !typeIds.isEmpty()) {
			sb.append(" AND t.id IN :typeIds");
			objects.put("typeIds", typeIds);
		}

		String where = null;
		if(objects.size() > 0) {
			where = sb.toString();
		} else {
			objects = null;
		}

		return listByWhere(where, objects, null, null, 0, 0);
	}
}
