/*
 * InsAgent - https://github.com/vykulakov/InsAgent
 *
 * Copyright 2017 Vyacheslav Kulakov
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

package ru.insagent.document.action;

import ru.insagent.action.BaseAction;
import ru.insagent.dao.UnitDao;
import ru.insagent.document.model.ActType;
import ru.insagent.model.Unit;
import ru.insagent.workflow.dao.ActionDao;
import ru.insagent.workflow.dao.LinkDao;
import ru.insagent.workflow.dao.NodeDao;
import ru.insagent.workflow.model.Action;
import ru.insagent.workflow.model.Link;
import ru.insagent.workflow.model.Node;

import java.util.*;

public class BsoJournalAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private List<Action> actions;

	public List<Action> getActions() {
		return actions;
	}

	private Set<ActType> types = new LinkedHashSet<>();

	public Set<ActType> getTypes() {
		return types;
	}

	private List<Node> nodes;

	public List<Node> getNodes() {
		return nodes;
	}

	private List<Unit> units;

	public List<Unit> getUnits() {
		return units;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin", "director", "manager", "register");
	}

	@Override
	public String executeImpl() {
		LinkDao ld = new LinkDao(conn);
		ActionDao ad = new ActionDao(conn);
		NodeDao nd = new NodeDao(conn);
		UnitDao ud = new UnitDao();

		ArrayList<Integer> nodeIds = new ArrayList<>();

		List<Link> links = ld.listByUser(baseUser);
		for (Link link : links) {
			if (link.getActType() != null) {
				types.add(link.getActType());
				nodeIds.add(link.getNodeFrom().getId());
				nodeIds.add(link.getNodeTo().getId());
			}
		}

		actions = ad.listByUser(baseUser);

		nodes = new ArrayList<>();
		for (Node node : nd.listByIds(nodeIds)) {
			if (node.getNodeType().getId() == 1 || node.getNodeType().getId() == 2) {
				continue;
			}

			nodes.add(node);
		}

		units = ud.listByUser(baseUser);

		return SUCCESS;
	}
}
