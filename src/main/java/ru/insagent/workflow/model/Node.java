package ru.insagent.workflow.model;

import java.util.ArrayList;
import java.util.List;

import ru.insagent.model.UnitType;
import ru.insagent.model.IdBase;
import ru.insagent.model.Unit;

public class Node extends IdBase {
	private static final long serialVersionUID = -9054275807494235590L;

	private String name;
	private NodeType nodeType;
	private UnitType unitType;
	private List<Unit> units = new ArrayList<Unit>();

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public NodeType getNodeType() {
		return nodeType;
	}
	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}
	public UnitType getUnitType() {
		return unitType;
	}
	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}
	public List<Unit> getUnits() {
		return units;
	}
	public void setUnits(List<Unit> units) {
		this.units = units;
	}

	public String toString() {
		//TODO Написать тело метода
		StringBuilder sb = new StringBuilder();
		sb.append("Node: id=");
		sb.append(id);
		sb.append("; name=");
		sb.append(name);

		return sb.toString();
	}
}
