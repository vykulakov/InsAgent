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
import ru.insagent.document.model.BsoArchived;

@Repository
public class BsoArchivedDao extends BsoDao<BsoArchived> {
    {
        clazz = BsoArchived.class;

        sortByMap.put("created", "b.created");
        sortByMap.put("series", "b.series");
        sortByMap.put("number", "b.number");
        sortByMap.put("nodeName", "b.node.name");
        sortByMap.put("unitName", "b.unit.name");
        sortByMap.put("issuedDate", "b.issuedDate");
        sortByMap.put("issuedBy", "CONCAT(b.issuedBy.firstName, b.issuedBy.lastName)");
        sortByMap.put("issuedUnitName", "b.issuedUnit.name");
        sortByMap.put("corruptedDate", "b.corruptedDate");
        sortByMap.put("corruptedBy", "CONCAT(b.corruptedBy.firstName, b.corruptedBy.lastName)");
        sortByMap.put("corruptedUnitName", "b.corruptedUnit.name");
        sortByMap.put("registeredDate", "b.registeredDate");
        sortByMap.put("registeredBy", "CONCAT(b.registeredBy.firstName, b.registeredBy.lastName)");
        sortByMap.put("registeredUnitName", "b.registeredBy.name");
        sortByMap.put("insured", "b.insured");
        sortByMap.put("premium", "b.premium");

        countQueryPrefix = ""
                + " SELECT"
                + "     COUNT(*) AS count"
                + " FROM"
                + "     BsoArchived b"
                + " WHERE"
                + "     1 = 1";

        selectQueryPrefix = ""
                + " SELECT"
                + "     b"
                + " FROM"
                + "     BsoArchived b"
                + " WHERE"
                + "     1 = 1";
    }
}
