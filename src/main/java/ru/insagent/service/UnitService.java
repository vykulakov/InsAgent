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
import ru.insagent.dao.UnitDao;
import ru.insagent.dao.UnitTypeDao;
import ru.insagent.exception.AppException;
import ru.insagent.management.unit.model.UnitFilter;
import ru.insagent.model.UnitType;
import ru.insagent.management.unit.model.UnitDTO;
import ru.insagent.model.Unit;
import ru.insagent.model.User;
import ru.insagent.util.Hibernate;

import java.util.List;

@Service
public class UnitService {
    private UnitDao unitDao;
    private UnitTypeDao unitTypeDao;

    @Autowired
    public void setUnitDao(UnitDao unitDao) {
        this.unitDao = unitDao;
    }

    @Autowired
    public void setUnitTypeDao(UnitTypeDao unitTypeDao) {
        this.unitTypeDao = unitTypeDao;
    }

    /**
     * Get rows count for the last query.
     *
     * @return Rows count for the last query.
     */
    public Long getCount() {
        return unitDao.getCount();
    }

    public List<UnitDTO> listByUser(User user, UnitFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
        List<UnitDTO> units;

        Hibernate.beginTransaction();
        try {
            units = unitDao.listByUser(user, filter, sortBy, sortDir, limitRows, limitOffset);

            Hibernate.commit();
        } catch (Exception e) {
            Hibernate.rollback();

            throw new AppException("Cannot get units", e);
        }

        return units;
    }

    public void update(Unit unit) {
        Hibernate.beginTransaction();
        try {
            unitDao.update(unit);

            Hibernate.commit();
        } catch (Exception e) {
            Hibernate.rollback();

            throw new AppException("Cannot update unit", e);
        }
    }

    public void remove(int unitId) {
        Hibernate.beginTransaction();
        try {
            unitDao.remove(unitId);

            Hibernate.commit();
        } catch (Exception e) {
            Hibernate.rollback();

            throw new AppException("Cannot remove unit", e);
        }
    }

    public List<UnitType> types() {
        List<UnitType> result;

        Hibernate.beginTransaction();
        try {
            result = unitTypeDao.list();

            Hibernate.commit();
        } catch (Exception e) {
            Hibernate.rollback();

            throw new AppException("Cannot get unit types", e);
        }

        return result;
    }
}
