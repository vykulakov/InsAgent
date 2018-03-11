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

import org.springframework.stereotype.Repository;
import ru.insagent.management.model.UnitType;

@Repository
public class UnitTypeDao extends SimpleHDao<UnitType> {
	{
		clazz = UnitType.class;

		countQueryPrefix = ""
			+ " SELECT"
			+ "     COUNT(*) AS count"
			+ " FROM"
			+ "     UnitType t"
			+ " WHERE"
			+ "     1 = 1";

		selectQueryPrefix = ""
			+ " SELECT"
			+ "     t"
			+ " FROM"
			+ "     UnitType t"
			+ " WHERE"
			+ "     1 = 1";
	}
}
