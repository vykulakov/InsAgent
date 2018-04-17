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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import ru.insagent.dao.SimpleDao;
import ru.insagent.dao.SimpleHDao;
import ru.insagent.model.IdBase;
import ru.insagent.model.UnitType;
import ru.insagent.workflow.model.Node;
import ru.insagent.workflow.model.NodeType;

@Repository
public class NodeDao extends SimpleHDao<Node> {
	{
        clazz = Node.class;

        countQueryPrefix = ""
				+ " SELECT"
				+ "     COUNT(*) AS count"
				+ " FROM"
				+ "     Node n"
				+ " WHERE"
				+ "     1 = 1";

		selectQueryPrefix = ""
				+ " SELECT"
				+ "     n"
				+ " FROM"
				+ "     Node n"
				+ " WHERE"
				+ "     1 = 1";
	}

    public List<Node> listByIds(List<Integer> nodeIds) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> objects = new HashMap<>();

        sb.append("n.id IN :ids");
        objects.put("ids", nodeIds);

        return listByWhere(sb.toString(), objects);
    }
}
