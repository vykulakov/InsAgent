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

package ru.insagent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.insagent.dao.CityDao;
import ru.insagent.exception.NotFoundException;
import ru.insagent.management.city.model.CityDTO;
import ru.insagent.management.city.model.CityFilter;
import ru.insagent.model.City;
import ru.insagent.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CityService {
    private CityDao cityDao;

    @Autowired
    public void setCityDao(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    public Long getCount() {
        return cityDao.getCount();
    }

    public List<City> list() {
        return cityDao.list();
    }

    public City getEditable(int id) {
        City city = cityDao.get(id);
        if (city == null) {
            throw new NotFoundException("City not found");
        }

        return city.makeEditableCopy();
    }

    public List<CityDTO> listByUser(User user, CityFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
        return cityDao
                .listByUser(user, filter, sortBy, sortDir, limitRows, limitOffset)
                .stream().map(CityDTO::new)
                .collect(Collectors.toList());
    }

    public void update(City newCity) {
        if (newCity.getId() == 0) {
            cityDao.add(newCity);
        } else {
            City oldCity = cityDao.get(newCity.getId());
            if (oldCity == null) {
                throw new NotFoundException("City not found");
            }

            oldCity.setName(newCity.getName());
            oldCity.setComment(newCity.getComment());
        }
    }

    public void remove(int id) {
        City city = cityDao.get(id);
        if (city == null) {
            throw new NotFoundException("City not found");
        }
        city.setRemoved(true);
    }
}
