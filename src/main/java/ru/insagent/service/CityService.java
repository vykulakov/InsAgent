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

package ru.insagent.service;

import java.util.List;

import ru.insagent.dao.CityDao;
import ru.insagent.exception.AppException;
import ru.insagent.management.model.CityFilter;
import ru.insagent.model.City;
import ru.insagent.model.User;
import ru.insagent.util.Hibernate;

public class CityService {
	final private CityDao cityDao = new CityDao();

	/**
	 * Get rows count for the last query.
	 * @return Rows count for the last query.
	 */
	public Long getCount() {
		return cityDao.getCount();
	}

	public List<City> listByUser(User user, String search, String sortBy, String sortDir, int limitRows, int limitOffset) {
		List<City> cities = null;

		Hibernate.beginTransaction();
		try {
			cities = cityDao.listByUser(user, search, sortBy, sortDir, limitRows, limitOffset);

			Hibernate.commit();
		} catch(Exception e) {
			Hibernate.rollback();

			throw new AppException("Cannot get cities", e);
		}

		return cities;
	}

	public List<City> listByUser(User user, CityFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
		List<City> cities = null;

		Hibernate.beginTransaction();
		try {
			cities = cityDao.listByUser(user, filter, sortBy, sortDir, limitRows, limitOffset);

			Hibernate.commit();
		} catch(Exception e) {
			Hibernate.rollback();

			throw new AppException("Cannot get cities", e);
		}

		return cities;
	}

	public void update(City city) {
		Hibernate.beginTransaction();
		try {
			cityDao.update(city);

			Hibernate.commit();
		} catch(Exception e) {
			Hibernate.rollback();

			throw new AppException("Cannot update city", e);
		}
	}

	public void remove(int cityId) {
		Hibernate.beginTransaction();
		try {
			cityDao.remove(cityId);

			Hibernate.commit();
		} catch(Exception e) {
			Hibernate.rollback();

			throw new AppException("Cannot remove city", e);
		}
	}
}
