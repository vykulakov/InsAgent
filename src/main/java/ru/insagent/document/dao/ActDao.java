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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.insagent.dao.SimpleHDao;
import ru.insagent.dao.UnitDao;
import ru.insagent.document.model.Act;
import ru.insagent.document.model.ActFilter;
import ru.insagent.model.IdBase;
import ru.insagent.model.Roles;
import ru.insagent.model.Unit;
import ru.insagent.model.User;
import ru.insagent.workflow.dao.LinkDao;
import ru.insagent.workflow.model.Link;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ActDao extends SimpleHDao<Act> {
    private LinkDao linkDao;
    private UnitDao unitDao;

    @Autowired
    public void setLinkDao(LinkDao linkDao) {
        this.linkDao = linkDao;
    }

    @Autowired
    public void setUnitDao(UnitDao unitDao) {
        this.unitDao = unitDao;
    }

    {
        clazz = Act.class;

        sortByMap.put("created",      "a.created");
        sortByMap.put("typeName",     "a.type.fullName");
        sortByMap.put("nodeFromName", "a.nodeFrom.name");
        sortByMap.put("nodeToName",   "a.nodeTo.name");
        sortByMap.put("unitFromName", "a.unitFrom.name");
        sortByMap.put("unitToName",   "a.unitTo.name");
        sortByMap.put("amount",       "a.amount");

        countQueryPrefix = ""
                + " SELECT"
                + "     COUNT(*) AS count"
                + " FROM"
                + "     Act a"
                + " WHERE"
                + "     1 = 1";

        selectQueryPrefix = ""
                + " SELECT"
                + "     a"
                + " FROM"
                + "     Act a"
                + " WHERE"
                + "     1 = 1";
    }

    public List<Act> list(Roles roles, ActFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
        List<Link> links = linkDao.listByRoles(roles);
        List<Unit> units = unitDao.listByRoles(roles);

        StringBuilder sb = new StringBuilder("");
        Map<String, Object> objects = new HashMap<>();

        sb.append("(");

        sb.append("(a.nodeFrom.id IN :nodeFromIds)");
        objects.put("nodeFromIds", links.stream().map(Link::getNodeFrom).map(IdBase::getId).collect(Collectors.toList()));

        sb.append(" AND ");

        sb.append("(a.unitFrom IS NULL OR a.unitFrom.id IN :unitFromIds)");
        objects.put("unitFromIds", units.stream().map(IdBase::getId).collect(Collectors.toList()));

        sb.append(") OR (");

        sb.append("(a.nodeTo.id IN :nodeToIds)");
        objects.put("nodeToIds", links.stream().map(Link::getNodeTo).map(IdBase::getId).collect(Collectors.toList()));

        sb.append(" AND ");

        sb.append("(a.unitTo IS NULL OR a.unitTo.id IN :unitToIds)");
        objects.put("unitToIds", units.stream().map(IdBase::getId).collect(Collectors.toList()));

        sb.append(")");

        return listByWhere(sb.toString(), objects, sortBy, sortDir, limitRows, limitOffset);
    }
}
