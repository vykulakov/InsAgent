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

import ru.insagent.model.MenuItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Menu item dao
 *
 * @author Kulakov Vyacheslav <kulakov.home@gmail.com>
 */
public class MenuItemDao extends SimpleHDao<MenuItem> {
	{
		clazz = MenuItem.class;

		countQueryPrefix = ""
			+ " SELECT"
			+ "     COUNT(*) AS count"
			+ " FROM"
			+ "     MenuItem i"
			+ "     LEFT JOIN i.roles r"
			+ " WHERE"
			+ "     1 = 1";

		selectQueryPrefix = ""
			+ " SELECT"
			+ "     i"
			+ " FROM"
			+ "     MenuItem i"
			+ "     LEFT JOIN i.roles r"
			+ " WHERE"
			+ "     1 = 1";

		selectOrder = "i.order";
	}

	/**
	 * Get menu items by roles idxes
	 * @param roles - role idxes.
	 * @return Menu items with given idxes.
	 */
	public List<MenuItem> listByRoleIdxes(Set<String> roles) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("idxes", roles);

		return listByWhere(" r.idx IN (:idxes) ", parameters);
	}
}
