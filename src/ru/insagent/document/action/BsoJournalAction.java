package ru.insagent.document.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ru.insagent.action.BaseAction;
import ru.insagent.document.model.ActType;
import ru.insagent.management.dao.UnitDao;
import ru.insagent.management.model.Unit;
import ru.insagent.workflow.dao.ActionDao;
import ru.insagent.workflow.dao.LinkDao;
import ru.insagent.workflow.dao.NodeDao;
import ru.insagent.workflow.model.Action;
import ru.insagent.workflow.model.Link;
import ru.insagent.workflow.model.Node;

public class BsoJournalAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	List<Action> actions;
	public List<Action> getActions() {
		return actions;
	}

	Set<ActType> types = new LinkedHashSet<ActType>();
	public Set<ActType> getTypes() {
		return types;
	}

	List<Node> nodes;
	public List<Node> getNodes() {
		return nodes;
	}

	List<Unit> units;
	public List<Unit> getUnits() {
		return units;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin", "director", "manager", "register");
		ALLOW_MSG = "У вас нет прав для работы с журналом БСО";
	}

	public String executeImpl() {
		LinkDao ld = new LinkDao(conn);
		ActionDao ad = new ActionDao(conn);
		NodeDao nd = new NodeDao(conn);
		UnitDao ud = new UnitDao(conn);

		ArrayList<Integer> nodeIds = new ArrayList<Integer>();

		List<Link> links = ld.listByUser(user);
		for(Link link : links) {
			if(link.getActType() != null) {
				types.add(link.getActType());
				nodeIds.add(link.getNodeFrom().getId());
				nodeIds.add(link.getNodeTo().getId());
			}
		}

		actions = ad.listByUser(user);

		nodes = new ArrayList<Node>();
		for(Node node : nd.listByIds(nodeIds)) {
			if(node.getNodeType().getId() == 1 || node.getNodeType().getId() == 2) {
				continue;
			}

			nodes.add(node);
		}

		units = ud.listByUser(user);

		return SUCCESS;
	}

	@Override
	public void validateImpl() {
	}
}
