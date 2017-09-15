package ru.insagent.management.model;

import ru.insagent.model.Filter;

public class CityFilter extends Filter {
	private static final long serialVersionUID = -1L;

	private String name;
	private Boolean removed;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getRemoved() {
		return removed;
	}
	public void setRemoved(Boolean removed) {
		this.removed = removed;
	}

	@Override
	public String toString() {
		return "CityFilter [name=" + name + ", removed=" + removed + "]";
	}
}
