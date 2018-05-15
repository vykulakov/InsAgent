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

package ru.insagent.document.dao;

import org.springframework.stereotype.Repository;
import ru.insagent.document.model.BsoNormal;
import ru.insagent.model.Roles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BsoNormalDao extends BsoDao<BsoNormal> {
    {
        clazz = BsoNormal.class;

        sortByMap.put("created", "b.created");
        sortByMap.put("series", "b.series");
        sortByMap.put("number", "b.number");
        sortByMap.put("issuedDate", "b.issuedDate");
        sortByMap.put("issuedBy", "CONCAT(b.issuedBy.firstName, b.issuedBy.lastName)");
        sortByMap.put("issuedUnit", "b.issuedUnit.name");
        sortByMap.put("corruptedDate", "b.corruptedDate");
        sortByMap.put("corruptedBy", "CONCAT(b.corruptedBy.firstName, b.corruptedBy.lastName)");
        sortByMap.put("corruptedUnit", "b.corruptedUnit.name");
        sortByMap.put("registeredDate", "b.registeredDate");
        sortByMap.put("registeredBy", "CONCAT(b.registeredBy.firstName, b.registeredBy.lastName)");
        sortByMap.put("registeredUnit", "b.registeredBy.name");
        sortByMap.put("insured", "b.insured");
        sortByMap.put("premium", "b.premium");
        sortByMap.put("nodeName", "b.node.name");
        sortByMap.put("unitName", "b.unit.name");

        countQueryPrefix = ""
                + " SELECT"
                + "     COUNT(*) AS count"
                + " FROM"
                + "     BsoNormal b"
                + " WHERE"
                + "     1 = 1";

        selectQueryPrefix = ""
                + " SELECT"
                + "     b"
                + " FROM"
                + "     BsoNormal b"
                + " WHERE"
                + "     1 = 1";
    }

    public BsoNormal get(String series, long number) {
        StringBuilder sb = new StringBuilder("");
        Map<String, Object> objects = new HashMap<>();

        sb.append("b.series = :series AND b.number = :number");
        objects.put("series", series);
        objects.put("number", number);

        List<BsoNormal> bsos = listByWhere(sb.toString(), objects);
        if (bsos.isEmpty()) {
            return null;
        } else {
            return bsos.get(0);
        }
    }

    public List<BsoNormal> listByRoles(Roles roles) {
        return listByRoles(roles, null, null, 0, 0);
    }

    public List<BsoNormal> listByRoles(Roles roles, String sortBy, String sortDir, int limitRows, int limitOffset) {
        return listByRoles(roles, null, sortBy, sortDir, limitRows, limitOffset);
    }
}
