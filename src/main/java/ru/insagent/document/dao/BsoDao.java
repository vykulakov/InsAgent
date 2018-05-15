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
import ru.insagent.document.model.Bso;
import ru.insagent.document.model.BsoFilter;
import ru.insagent.model.IdBase;
import ru.insagent.model.Roles;
import ru.insagent.model.Unit;
import ru.insagent.workflow.dao.LinkDao;
import ru.insagent.workflow.model.Link;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class BsoDao<T extends Bso> extends SimpleHDao<T> {
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

    public List<T> listByRoles(Roles roles, BsoFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
        List<Link> links = linkDao.listByRoles(roles);
        List<Unit> units = unitDao.listByRoles(roles);

        StringBuilder sb = new StringBuilder("");
        Map<String, Object> objects = new HashMap<>();

        if (!units.isEmpty()) {
            sb.append("(b.node.nodeType.id = 4 AND b.unit IS NOT NULL AND b.unit.id IN :unitIds)");
            objects.put("unitIds", units.stream().map(IdBase::getId).collect(Collectors.toList()));
        }

        if (!units.isEmpty() && !links.isEmpty()) {
            sb.append(" OR ");
        }

        if (!links.isEmpty()) {
            List<Integer> nodeIds = Stream.concat(
                    links.stream().map(Link::getNodeTo).map(IdBase::getId),
                    links.stream().map(Link::getNodeFrom).map(IdBase::getId)
            ).collect(Collectors.toList());

            sb.append("(b.node.nodeType.id != 4 AND b.unit IS NULL AND b.node.id IN :nodeIds)");
            objects.put("nodeIds", nodeIds);
        }

        if (filter != null) {
            sb.insert(0, "(");
            sb.append(") AND (");

            sb.append("1 = 1");
            if (filter.getSearch() != null && !filter.getSearch().trim().isEmpty()) {
                sb.append(" AND (");
                sb.append(" b.series LIKE :search OR");
                sb.append(" b.number LIKE :search OR");
                sb.append(" b.node.name LIKE :search OR");
                sb.append(" o.name LIKE :search");
                sb.append(" )");
                objects.put("search", "%" + filter.getSearch().trim() + "%");
            }
            if (filter.getSeries() != null) {
                sb.append(" AND b.series LIKE :series");
                objects.put("series", filter.getSeries().replace("*", "%"));
            }
            if (filter.getNumberFrom() != null) {
                sb.append(" AND b.number >= :numberFrom");
                objects.put("numberFrom", filter.getNumberFrom());
            }
            if (filter.getNumberTo() != null) {
                sb.append(" AND b.number <= :numberTo");
                objects.put("numberTo", filter.getNumberTo());
            }
            if (filter.getInsured() != null) {
                sb.append(" AND b.insured LIKE :insured");
                objects.put("insured", filter.getInsured().replace("*", "%"));
            }
            if (filter.getPremiumFrom() != null) {
                sb.append(" AND b.premium >= :premiumFrom");
                objects.put("premiumFrom", filter.getPremiumFrom());
            }
            if (filter.getPremiumTo() != null) {
                sb.append(" AND b.premium <= :premiumTo");
                objects.put("premiumTo", filter.getPremiumTo());
            }
            if (filter.getNodes() != null && !filter.getNodes().isEmpty()) {
                sb.append(" AND b.nodeId IN :nodeIds");
                objects.put("nodeIds", filter.getNodes().stream().map(IdBase::getId).collect(Collectors.toList()));
            }
            if (filter.getUnits() != null && !filter.getUnits().isEmpty()) {
                sb.append(" AND b.unitId IN :unitIds");
                objects.put("unitIds", filter.getUnits().stream().map(IdBase::getId).collect(Collectors.toList()));
            }
            if (filter.getCreatedFrom() != null) {
                sb.append(" AND b.created >= :createdFrom");
                objects.put("createdFrom", filter.getCreatedFrom());
            }
            if (filter.getCreatedTo() != null) {
                sb.append(" AND b.created <= :createdTo");
                objects.put("createdTo", filter.getCreatedTo());
            }
            if (filter.getIssuedFrom() != null) {
                sb.append(" AND b.issuedDate >= :issuedFrom");
                objects.put("issuedFrom", filter.getIssuedFrom());
            }
            if (filter.getIssuedTo() != null) {
                sb.append(" AND b.issuedDate <= :issuedTo");
                objects.put("issuedTo", filter.getIssuedTo());
            }
            if (filter.getCorruptedFrom() != null) {
                sb.append(" AND b.corruptedDate >= :corruptedFrom");
                objects.put("corruptedFrom", filter.getCorruptedFrom());
            }
            if (filter.getCorruptedTo() != null) {
                sb.append(" AND b.corruptedDate <= :corruptedTo");
                objects.put("corruptedTo", filter.getCorruptedTo());
            }
            if (filter.getRegisteredFrom() != null) {
                sb.append(" AND b.registeredDate >= :registeredFrom");
                objects.put("registeredFrom", filter.getRegisteredFrom());
            }
            if (filter.getRegisteredTo() != null) {
                sb.append(" AND b.registeredDate <= :registeredTo");
                objects.put("registeredTo", filter.getRegisteredTo());
            }

            sb.append(")");
        }

        return listByWhere(sb.toString(), objects, sortBy, sortDir, limitRows, limitOffset);
    }

    public List<T> listBySeriesAndNumbers(String series, long numberFrom, long numberTo) {
        String where = "b.series = :series AND :numberFrom <= b.number AND b.number <= :numberTo";
        Map<String, Object> objects = new HashMap<>();
        objects.put("series", series);
        objects.put("numberFrom", numberFrom);
        objects.put("numberTo", numberTo);

        return listByWhere(where, objects);
    }
}
