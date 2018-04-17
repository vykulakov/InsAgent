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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.insagent.management.unit.model.UnitFilter;
import ru.insagent.model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class UnitDao extends SimpleHDao<Unit> {
    {
        clazz = Unit.class;

        sortByMap.put("id", "u.id");
        sortByMap.put("name", "u.name");
        sortByMap.put("typeName", "u.type.name");
        sortByMap.put("cityName", "u.city.name");

        countQueryPrefix = ""
                + " SELECT"
                + "     COUNT(*) AS count"
                + " FROM"
                + "     Unit u"
                + "     LEFT JOIN u.type t"
                + "     LEFT JOIN u.city c"
                + " WHERE"
                + "     1 = 1";

        selectQueryPrefix = ""
                + " SELECT"
                + "     u"
                + " FROM"
                + "     Unit u"
                + "     LEFT JOIN u.type t"
                + "     LEFT JOIN u.city c"
                + " WHERE"
                + "     1 = 1";
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

    public List<Unit> listByRoles(Roles roles) {
        return listByWhere(null, null, null, null, 0, 0);
    }

    public List<Unit> listByRoles(Roles roles, String sortBy, String sortDir, int limitRows, int limitOffset) {
        return listByWhere(null, null, sortBy, sortDir, limitRows, limitOffset);
    }

    public List<Unit> listByRoles(Roles roles, UnitFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> objects = new HashMap<>();

        if (filter != null) {
            sb.append("1 = 1");
            if (!filter.isRemoved()) {
                sb.append(" AND u.removed = 0");
            }
            if (StringUtils.isNotBlank(filter.getName())) {
                sb.append(" AND u.name LIKE :name");
                objects.put("name", "%" + filter.getName().replace("*", "%") + "%");
            }
            if (filter.getTypes() != null && !filter.getTypes().isEmpty()) {
                sb.append(" AND u.type.id IN :typeIds");
                objects.put("typeIds", filter.getTypes().stream().map(UnitType::getId).collect(Collectors.toList()));
            }
            if (filter.getCities() != null && !filter.getCities().isEmpty()) {
                sb.append(" AND u.city.id IN :cityIds");
                objects.put("cityIds", filter.getCities().stream().map(City::getId).collect(Collectors.toList()));
            }
            if (StringUtils.isNotBlank(filter.getSearch())) {
                sb.append(" AND (");
                sb.append(" u.name LIKE :search OR");
                sb.append(" u.type.name LIKE :search OR");
                sb.append(" u.city.name LIKE :search");
                sb.append(" )");
                objects.put("search", "%" + filter.getSearch().trim() + "%");
            }
        }

        return listByWhere(sb.toString(), objects, sortBy, sortDir, limitRows, limitOffset);
    }

    public List<Unit> listByTypeIds(Collection<Integer> typeIds) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> objects = new HashMap<>();

        if (typeIds != null && !typeIds.isEmpty()) {
            sb.append(" AND t.id IN :typeIds");
            objects.put("typeIds", typeIds);
        }

        return listByWhere(sb.toString(), objects, null, null, 0, 0);
    }
}
