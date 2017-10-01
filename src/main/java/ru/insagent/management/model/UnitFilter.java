package ru.insagent.management.model;

import java.util.List;

import ru.insagent.model.City;
import ru.insagent.model.Filter;

public class UnitFilter extends Filter {
	private static final long serialVersionUID = -1L;

	private String name;
	private List<UnitType> types;
	private List<City> cities;
	private Boolean removed;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<UnitType> getTypes() {
		return types;
	}
	public void setTypes(List<UnitType> types) {
		this.types = types;
	}
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	public Boolean getRemoved() {
		return removed;
	}
	public void setRemoved(Boolean removed) {
		this.removed = removed;
	}

	@Override
	public String toString() {
		return "UnitFilter [name=" + name + ", types=" + types + ", cities=" + cities + ", removed=" + removed + "]";
	}
}
