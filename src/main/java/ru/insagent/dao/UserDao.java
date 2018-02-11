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

package ru.insagent.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.insagent.management.model.UserFilter;
import ru.insagent.model.Unit;
import ru.insagent.model.User;

/**
 * User DAO.
 */
public class UserDao extends SimpleHDao<User> {
	{
		clazz =  User.class;

		sortByMap.put("id", "u.id");
		sortByMap.put("username", "u.username");
		sortByMap.put("firstName", "u.firstName");
		sortByMap.put("lastName", "u.lastName");
		sortByMap.put("lastAuth", "u.lastAuth");
		sortByMap.put("unit", "u.unit.name");

		countQueryPrefix = ""
			+ " SELECT"
			+ "     COUNT(*) AS count"
			+ " FROM"
			+ "     User u"
			+ " WHERE"
			+ "     1 = 1";

		selectQueryPrefix = ""
			+ " SELECT"
			+ "     u"
			+ " FROM"
			+ "     User u"
			+ " WHERE"
			+ "     1 = 1";
	}

	public User getByUsername(String username) {
		Map<String, Object> objects = new HashMap<String, Object>();
		objects.put("username", username);

		List<User> users = listByWhere("u.username = :username", objects);
		if(users.isEmpty()) {
			return null;
		} else {
			return users.get(0);
		}
	}

	public List<User> listByUser(User user) {
		return listByUser(user, null, null, 0, 0);
	}

	public List<User> listByUser(User user, String sortBy, String sortDir, int limitRows, int limitOffset) {
		return listByUser(user, (String) null, sortBy, sortDir, limitRows, limitOffset);
	}

	public List<User> listByUser(User user, String search, String sortBy, String sortDir, int limitRows, int limitOffset) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> objects = new HashMap<String, Object>();

		if(search != null && !search.trim().isEmpty()) {
			search = "%" + search + "%";

			sb.append("u.username LIKE :search OR");
			sb.append("u.firstName LIKE :search OR");
			sb.append("u.lastName LIKE :search");
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

	public List<User> listByUser(User user, UserFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> objects = new HashMap<String, Object>();

		if(filter != null) {
			sb.append("1 = 1");
			if(filter.getLogin() != null) {
				sb.append(" AND u.username LIKE :username");
				objects.put("username", "%" + filter.getLogin().replace("*", "%") + "%");
			}
			if(filter.getName() != null) {
				sb.append(" AND CONCAT(u.firstName, ' ', u.lastName) LIKE :name OR CONCAT(u.lastName, ' ', u.firstName) LIKE :name");
				objects.put("name", "%" + filter.getName().replace("*", "%") + "%");
			}
			if(filter.getUnits() != null && !filter.getUnits().isEmpty()) {
				sb.append(" AND u.unitId IN :unitIds");
				objects.put("unitIds", filter.getUnits().stream().map(Unit::getId).collect(Collectors.toList()));
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
