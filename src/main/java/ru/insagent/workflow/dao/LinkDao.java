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

package ru.insagent.workflow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import ru.insagent.dao.SimpleDao;
import ru.insagent.dao.SimpleHDao;
import ru.insagent.document.model.ActType;
import ru.insagent.exception.AppException;
import ru.insagent.model.IdBase;
import ru.insagent.model.Role;
import ru.insagent.model.Roles;
import ru.insagent.model.User;
import ru.insagent.util.JdbcUtils;
import ru.insagent.workflow.model.Link;
import ru.insagent.workflow.model.Node;

@Repository
public class LinkDao extends SimpleHDao<Link> {
    {
        clazz = Link.class;

        countQueryPrefix = ""
                + " SELECT"
                + "     COUNT(*) AS count"
                + " FROM"
                + "     Link l"
                + "     JOIN l.roles r"
                + " WHERE"
                + "     1 = 1";

        selectQueryPrefix = ""
                + " SELECT"
                + "     l"
                + " FROM"
                + "     Link l"
                + "     JOIN l.roles r"
                + " WHERE"
                + "     1 = 1";
    }

    public List<Link> listByRoles(Roles roles) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> objects = new HashMap<>();

        sb.append("r.idx IN :idxes");
        objects.put("idxes", roles);

        return listByWhere(sb.toString(), objects);
    }

    public List<String> getRolesByLinkId(int linkId) throws AppException {
        return get(linkId).getRoles().stream().map(Role::getIdx).collect(Collectors.toList());
    }

    public List<ActType> listActTypesByUser(User user) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> objects = new HashMap<>();

        sb.append("l.role.id IN :ids");
        objects.put("ids", user.getRoles().stream().map(IdBase::getId).collect(Collectors.toList()));

        return listByWhere(sb.toString(), objects).stream().map(Link::getActType).collect(Collectors.toList());
    }
}
