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

import org.springframework.stereotype.Repository;
import ru.insagent.management.user.model.UserFilter;
import ru.insagent.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User DAO.
 */
@Repository
public class UserDao extends SimpleHDao<User> {
    {
        clazz = User.class;

        sortByMap.put("id", "u.id");
        sortByMap.put("login", "u.username");
        sortByMap.put("name", "CONCAT(u.firstName, u.lastName)");
        sortByMap.put("unitName", "u.unit.name");

        countQueryPrefix = ""
                + " SELECT"
                + "     COUNT(*) AS count"
                + " FROM"
                + "     User u"
                + " WHERE"
                + "     1 = 1";

        selectQueryPrefix = ""
                + " SELECT"
                + "     u"
                + " FROM"
                + "     User u"
                + " WHERE"
                + "     1 = 1";
    }

    public User getByUsername(String username) {
        Map<String, Object> objects = new HashMap<>();
        objects.put("username", username);

        List<User> users = listByWhere("u.username = :username", objects);
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }

    public List<User> listByUser(User user) {
        return listByUser(user, null, null, 0, 0);
    }

    public List<User> listByUser(User user, String sortBy, String sortDir, int limitRows, int limitOffset) {
        return listByUser(user, null, sortBy, sortDir, limitRows, limitOffset);
    }

    public List<User> listByUser(User user, UserFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> objects = new HashMap<>();

        if (filter != null) {
            sb.append("1 = 1");
            if (filter.getLogin() != null) {
                sb.append(" AND u.username LIKE :username");
                objects.put("username", "%" + filter.getLogin().replace("*", "%") + "%");
            }
            if (filter.getName() != null) {
                sb.append(" AND CONCAT(u.firstName, ' ', u.lastName) LIKE :name OR CONCAT(u.lastName, ' ', u.firstName) LIKE :name");
                objects.put("name", "%" + filter.getName().replace("*", "%") + "%");
            }
            if (filter.getUnitIds() != null && !filter.getUnitIds().isEmpty()) {
                sb.append(" AND u.unit.id IN :unitIds");
                objects.put("unitIds", filter.getUnitIds());
            }
            if (filter.getSearch() != null && !filter.getSearch().trim().isEmpty()) {
                sb.append(" AND (");
                sb.append(" u.username LIKE :search OR");
                sb.append(" u.firstName LIKE :search OR");
                sb.append(" u.lastName LIKE :search");
                sb.append(" )");
                objects.put("search", "%" + filter.getSearch().trim() + "%");
            }
        }

        String where = null;
        if (objects.size() > 0) {
            where = sb.toString();
        } else {
            objects = null;
        }

        return listByWhere(where, objects, sortBy, sortDir, limitRows, limitOffset);
    }
}
