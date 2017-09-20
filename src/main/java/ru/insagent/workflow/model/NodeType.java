package ru.insagent.workflow.model;

import ru.insagent.model.IdBase;

public class NodeType extends IdBase {
	private static final long serialVersionUID = -9054275807494235590L;

	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Node: id=");
		sb.append(id);
		sb.append("; name=");
		sb.append(name);

		return sb.toString();
	}
}
