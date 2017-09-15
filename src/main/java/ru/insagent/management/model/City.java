package ru.insagent.management.model;

import ru.insagent.model.Base;

public class City extends Base {
	private static final long serialVersionUID = -4521788722648020946L;

	private String name;
	private String comment;
	private boolean removed;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public boolean isRemoved() {
		return removed;
	}
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + ", comment=" + comment + ", removed=" + removed + "]";
	}
}
