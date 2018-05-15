/*
 * InsAgent - https://github.com/vykulakov/InsAgent
 *
 * Copyright 2018 Vyacheslav Kulakov
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
import ru.insagent.dao.UnitDao;
import ru.insagent.document.dao.BsoArchivedDao;
import ru.insagent.document.dao.BsoNormalDao;
import ru.insagent.document.model.ActType;
import ru.insagent.document.model.BsoDTO;
import ru.insagent.document.model.BsoFilter;
import ru.insagent.model.Roles;
import ru.insagent.model.Unit;
import ru.insagent.workflow.dao.ActionDao;
import ru.insagent.workflow.dao.LinkDao;
import ru.insagent.workflow.dao.NodeDao;
import ru.insagent.workflow.model.Action;
import ru.insagent.workflow.model.Link;
import ru.insagent.workflow.model.Node;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArchivedService {
    private UnitDao unitDao;
    private NodeDao nodeDao;
    private LinkDao linkDao;
    private ActionDao actionDao;
    private BsoArchivedDao archivedDao;

    @Autowired
    public void setUnitDao(UnitDao unitDao) {
        this.unitDao = unitDao;
    }

    @Autowired
    public void setNodeDao(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }

    @Autowired
    public void setLinkDao(LinkDao linkDao) {
        this.linkDao = linkDao;
    }

    @Autowired
    public void setActionDao(ActionDao actionDao) {
        this.actionDao = actionDao;
    }

    @Autowired
    public void setArchivedDao(BsoArchivedDao archivedDao) {
        this.archivedDao = archivedDao;
    }

    /**
     * Get rows count for the last query.
     *
     * @return Rows count for the last query.
     */
    public Long getCount() {
        return archivedDao.getCount();
    }

    public List<BsoDTO> listByRoles(Roles roles, BsoFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
        return archivedDao
                .listByRoles(roles, filter, sortBy, sortDir, limitRows, limitOffset)
                .stream().map(BsoDTO::new)
                .collect(Collectors.toList());
    }


    public List<Unit> units(Roles roles) {
        return unitDao.listByRoles(roles);
    }

    public Collection<ActType> types(Roles roles) {
        Set<ActType> types = new HashSet<>();

        List<Link> links = linkDao.listByRoles(roles);
        for (Link link : links) {
            if (link.getActType() != null) {
                types.add(link.getActType());
            }
        }

        return types;
    }

    public List<Node> nodes(Roles roles) {
        List<Integer> nodeIds = new ArrayList<>();
        nodeIds.add(-1);

        List<Link> links = linkDao.listByRoles(roles);
        for (Link link : links) {
            if (link.getActType() != null) {
                nodeIds.add(link.getNodeFrom().getId());
                nodeIds.add(link.getNodeTo().getId());
            }
        }

        List<Node> nodes = new ArrayList<>();
        for (Node node : nodeDao.listByIds(nodeIds)) {
            if (node.getNodeType().getId() == 1 || node.getNodeType().getId() == 2) {
                continue;
            }

            nodes.add(node);
        }

        return nodes;
    }

    public List<Action> actions(Roles roles) {
        return actionDao.listByRoles(roles);
    }
}
