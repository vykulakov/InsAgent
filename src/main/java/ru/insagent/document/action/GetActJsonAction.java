package ru.insagent.document.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

import ru.insagent.action.BaseAction;
import ru.insagent.dao.UnitDao;
import ru.insagent.model.Entry;
import ru.insagent.model.Unit;
import ru.insagent.workflow.dao.LinkDao;
import ru.insagent.workflow.dao.NodeDao;
import ru.insagent.workflow.model.Item;
import ru.insagent.workflow.model.Link;
import ru.insagent.workflow.model.Node;
import ru.insagent.workflow.model.NodeType;

public class GetActJsonAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	List<Entry<Node, Node>> links = new ArrayList<Entry<Node, Node>>();
	// JavaScript не поддерживает объекты в качестве ключей - только строки.
	public List<Entry<Integer, Integer>> getLinks() {
		List<Entry<Integer, Integer>> result = new ArrayList<Entry<Integer, Integer>>();
		for(Entry<Node, Node> entry : links) {
			result.add(new Entry<Integer, Integer>(entry.getKey().getId(), entry.getValue().getId()));
		}
		return result;
	}
	
	private List<Item> itemsFrom = new ArrayList<Item>();
	public List<Item> getItemsFrom() {
		return itemsFrom;
	}

	private	List<Item> itemsTo = new ArrayList<Item>();
	public List<Item> getItemsTo() {
		return itemsTo;
	}

	private Integer actTypeId;
	public Integer getActTypeId() {
		return actTypeId;
	}
	@RequiredFieldValidator(message = "Идентификатор типа акта не передан", shortCircuit = true)
	@ConversionErrorFieldValidator(message = "Невозможно преобразовать идентификатор типа акта.", shortCircuit = true)
	@IntRangeFieldValidator(message = "Идентификатор типа акта имеет недопустимое значение.", min = "0", max = "255", shortCircuit = true)
	public void setActTypeId(Integer actTypeId) {
		this.actTypeId = actTypeId;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin", "director", "manager", "register");
		ALLOW_MSG = "У вас нет прав для получения данных акта.";
	}

	@Override
	public String executeImpl() {
		NodeDao nd = new NodeDao(conn);
		LinkDao ld = new LinkDao(conn);
		UnitDao ud = new UnitDao(conn);

		List<Unit> units = ud.listByUser(user);

		for(Link link : ld.listByUser(user)) {
			if(link.getActType() != null && link.getActType().getId() == actTypeId) {
				Node nodeFrom = nd.getById(link.getNodeFrom().getId());
				Node nodeTo = nd.getById(link.getNodeTo().getId());

				for(Unit unit : units) {
					if(nodeFrom.getNodeType().getId() == 4 && nodeFrom.getUnitType().equals(unit.getType())) {
						nodeFrom.getUnits().add(unit);
					}
					if(nodeTo.getNodeType().getId() == 4 && nodeTo.getUnitType().equals(unit.getType())) {
						nodeTo.getUnits().add(unit);
					}
				}

				links.add(new Entry<Node, Node>(nodeFrom, nodeTo));
			}
		}

		Map<NodeType, Item> nodeToItemFrom = new HashMap<NodeType, Item>();
		Map<NodeType, Item> nodeToItemTo = new HashMap<NodeType, Item>();
		Map<Unit, Item> unitToItemFrom = new HashMap<Unit, Item>();
		Map<Unit, Item> unitToItemTo = new HashMap<Unit, Item>();
		for(Entry<Node, Node> entry : links) {
			Item item;

			Node nodeFrom = entry.getKey();
			switch(nodeFrom.getNodeType().getId()) {
				case 1:
				case 2:
				case 3:
					if(nodeToItemFrom.containsKey(nodeFrom.getNodeType())) {
						item = nodeToItemFrom.get(nodeFrom.getNodeType());
						item.addNodeId(nodeFrom.getId());
					} else {
						item = new Item();
						item.addNodeId(nodeFrom.getId());
						item.setNodeTypeId(nodeFrom.getNodeType().getId());
						item.setId(nodeFrom.getId());
						item.setName(nodeFrom.getNodeType().getName());

						itemsFrom.add(item);
						nodeToItemFrom.put(nodeFrom.getNodeType(), item);
					}

					break;
				case 4:
					for(Unit unit : nodeFrom.getUnits()) {
						if(unitToItemFrom.containsKey(unit)) {
							item = unitToItemFrom.get(unit);
							item.addNodeId(nodeFrom.getId());
						} else {
							item = new Item();
							item.addNodeId(nodeFrom.getId());
							item.setNodeTypeId(nodeFrom.getNodeType().getId());
							item.setId(unit.getId());
							item.setName(unit.getName());
							if(unit.getType().getId() != 1) {
								item.setCityId(unit.getCity().getId());
								item.setCityName(unit.getCity().getName());
							}

							itemsFrom.add(item);

							unitToItemFrom.put(unit, item);
						}
					}
					break;
				default:
					addActionError("Найден неизвестный тип узла.");
					return ERROR;
			}

			Node nodeTo = entry.getValue();
			switch(nodeTo.getNodeType().getId()) {
				case 1:
				case 2:
				case 3:
					if(nodeToItemTo.containsKey(nodeTo.getNodeType())) {
						item = nodeToItemTo.get(nodeTo.getNodeType());
						item.addNodeId(nodeTo.getId());
					} else {
						item = new Item();
						item.addNodeId(nodeTo.getId());
						item.setNodeTypeId(nodeTo.getNodeType().getId());
						item.setId(nodeTo.getId());
						item.setName(nodeTo.getNodeType().getName());

						itemsTo.add(item);
						nodeToItemTo.put(nodeTo.getNodeType(), item);
					}

					break;
				case 4:
					for(Unit unit : nodeTo.getUnits()) {
						if(unitToItemTo.containsKey(unit)) {
							item = unitToItemTo.get(unit);
							item.addNodeId(nodeTo.getId());
						} else {
							item = new Item();
							item.addNodeId(nodeTo.getId());
							item.setNodeTypeId(nodeTo.getNodeType().getId());
							item.setId(unit.getId());
							item.setName(unit.getName());
							if(unit.getType().getId() != 1) {
								item.setCityId(unit.getCity().getId());
								item.setCityName(unit.getCity().getName());
							}

							itemsTo.add(item);

							unitToItemTo.put(unit, item);
						}
					}
					break;
				default:
					addActionError("Найден неизвестный тип узла.");
					return ERROR;
			}
		}

		return SUCCESS;
	}

	@Override
	public void validateImpl() {
	}
}
