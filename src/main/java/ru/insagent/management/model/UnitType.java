package ru.insagent.management.model;

import ru.insagent.model.Base;

public class UnitType extends Base {
	private static final long serialVersionUID = 4580145163020300664L;

	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "UnitType [id=" + id + ", name=" + name + "]";
	}
}
