package ru.insagent.management.model;

import java.util.List;

import ru.insagent.model.Filter;

public class UserFilter extends Filter {
	private static final long serialVersionUID = -1L;

	private String login;
	private String name;
	private List<Unit> units;
	private Boolean removed;

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Unit> getUnits() {
		return units;
	}
	public void setUnits(List<Unit> units) {
		this.units = units;
	}
	public Boolean getRemoved() {
		return removed;
	}
	public void setRemoved(Boolean removed) {
		this.removed = removed;
	}

	@Override
	public String toString() {
		return "UserFilter [login=" + login + ", name=" + name + ", units=" + units + ", removed=" + removed + "]";
	}
}
