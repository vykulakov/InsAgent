package ru.insagent.workflow.model;

import java.util.HashSet;
import java.util.Set;

import ru.insagent.document.model.ActType;
import ru.insagent.model.IdBase;

public class Link extends IdBase {
	private static final long serialVersionUID = -4192191224685061836L;

	private String name;
	private Node nodeFrom;
	private Node nodeTo;
	private ActType actType;
	private String comment;
	private Set<String> roles = new HashSet<String>();

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Node getNodeFrom() {
		return nodeFrom;
	}
	public void setNodeFrom(Node nodeFrom) {
		this.nodeFrom = nodeFrom;
	}
	public Node getNodeTo() {
		return nodeTo;
	}
	public void setNodeTo(Node nodeTo) {
		this.nodeTo = nodeTo;
	}
	public ActType getActType() {
		return actType;
	}
	public void setActType(ActType actType) {
		this.actType = actType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Link: id=");
		sb.append(id);
		sb.append("; name=");
		sb.append(name);

		return sb.toString();
	}
}
