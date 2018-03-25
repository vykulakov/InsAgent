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
import ru.insagent.dao.UnitDao;
import ru.insagent.dao.UnitTypeDao;
import ru.insagent.exception.NotFoundException;
import ru.insagent.management.city.model.CityDTO;
import ru.insagent.management.unit.model.UnitDTO;
import ru.insagent.management.unit.model.UnitFilter;
import ru.insagent.model.City;
import ru.insagent.model.Unit;
import ru.insagent.model.UnitType;
import ru.insagent.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UnitService {
    private CityDao cityDao;
    private UnitDao unitDao;
    private UnitTypeDao unitTypeDao;

    @Autowired
    public void setCityDao(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @Autowired
    public void setUnitDao(UnitDao unitDao) {
        this.unitDao = unitDao;
    }

    @Autowired
    public void setUnitTypeDao(UnitTypeDao unitTypeDao) {
        this.unitTypeDao = unitTypeDao;
    }

    public List<UnitType> types() {
        return unitTypeDao.list();
    }

    /**
     * Get rows count for the last query.
     *
     * @return Rows count for the last query.
     */
    public Long getCount() {
        return unitDao.getCount();
    }

    public Unit getEditable(int id) {
        Unit unit = unitDao.get(id);
        if (unit == null) {
            throw new NotFoundException("Unit not found");
        }

        return unit.makeEditableCopy();
    }

    public List<UnitDTO> listByUser(User user, UnitFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
        return unitDao
                .listByUser(user, filter, sortBy, sortDir, limitRows, limitOffset)
                .stream().map(UnitDTO::new)
                .collect(Collectors.toList());
    }

    public void update(Unit newUnit) {
        City city = cityDao.get(newUnit.getCity().getId());
        if (city == null) {
            throw new NotFoundException("City not found");
        }

        UnitType type = unitTypeDao.get(newUnit.getType().getId());
        if (type == null) {
            throw new NotFoundException("Type not found");
        }

        newUnit.setCity(city);
        newUnit.setType(type);
        if (newUnit.getId() == 0) {
            unitDao.add(newUnit);
        } else {
            Unit oldUnit = unitDao.get(newUnit.getId());
            if (oldUnit == null) {
                throw new NotFoundException("Unit not found");
            }

            oldUnit.setName(newUnit.getName());
            oldUnit.setComment(newUnit.getComment());
        }
    }

    public void remove(int id) {
        Unit unit = unitDao.get(id);
        if (unit == null) {
            throw new NotFoundException("Unit not found");
        }

        unit.setRemoved(true);
    }
}
