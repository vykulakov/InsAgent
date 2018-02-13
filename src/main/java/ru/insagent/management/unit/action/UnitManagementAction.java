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

package ru.insagent.management.unit.action;

import ru.insagent.action.BaseAction;
import ru.insagent.management.model.UnitType;
import ru.insagent.model.City;
import ru.insagent.service.CityService;
import ru.insagent.service.UnitService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnitManagementAction extends BaseAction {
	private static final long serialVersionUID = -2440800695359237060L;

	{
		ALLOW_ROLES = Arrays.asList("admin");
	}

	private List<UnitType> types = new ArrayList<>();

	public List<UnitType> getTypes() {
		return types;
	}

	private List<City> cities = new ArrayList<>();

	public List<City> getCities() {
		return cities;
	}

	@Override
	public String executeImpl() {
		CityService cs = new CityService();
		UnitService us = new UnitService();

		types = us.types();
		cities = cs.list();

		return SUCCESS;
	}
}
