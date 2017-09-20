package ru.insagent.management.model;

import ru.insagent.model.IdBase;

public class Unit extends IdBase {
	private static final long serialVersionUID = -6544474496483192296L;

	private String name;
	private UnitType type;
	private City city;
	private String comment;
	private boolean removed;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UnitType getType() {
		return type;
	}
	public void setType(UnitType type) {
		this.type = type;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
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
		return "Unit [id=" + id + ", name=" + name + ", type=" + type + ", city=" + city + ", comment=" + comment + ", removed=" + removed + "]";
	}
}
