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

        sortByMap.put("created", "bsoCreated");
        sortByMap.put("series", "bsoSeries");
        sortByMap.put("number", "bsoNumber");
        sortByMap.put("issuedDate", "bsoIssuedDate");
        sortByMap.put("issuedBy", "CONCAT(bsoIssuedUserFirstName,bsoIssuedUserLastName)");
        sortByMap.put("issuedUnit", "bsoIssuedUnitName");
        sortByMap.put("corruptedDate", "bsoCorruptedDate");
        sortByMap.put("corruptedBy", "CONCAT(bsoCorruptedUserFirstName,bsoCorruptedUserLastName)");
        sortByMap.put("corruptedUnit", "bsoCorruptedUnitName");
        sortByMap.put("registeredDate", "bsoRegisteredDate");
        sortByMap.put("registeredBy", "CONCAT(bsoRegisteredUserFirstName,bsoRegisteredUserLastName)");
        sortByMap.put("registeredUnit", "bsoRegisteredUnitName");
        sortByMap.put("insured", "bsoInsured");
        sortByMap.put("premium", "bsoPremium");
        sortByMap.put("node", "nodeName");
        sortByMap.put("unit", "unitName");

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
