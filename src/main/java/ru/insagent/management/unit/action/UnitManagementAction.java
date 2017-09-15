package ru.insagent.management.unit.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.insagent.action.BaseAction;
import ru.insagent.management.dao.CityDao;
import ru.insagent.management.dao.TypeDao;
import ru.insagent.management.model.City;
import ru.insagent.management.model.UnitType;

public class UnitManagementAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	{
		ALLOW_ROLES = Arrays.asList("admin");
		ALLOW_MSG = "У вас нет прав для управления подразделениями";
	}

	private List<UnitType> types = new ArrayList<UnitType>();
	public List<UnitType> getTypes() {
		return types;
	}

	private List<City> cities = new ArrayList<City>();
	public List<City> getCities() {
		return cities;
	}

	@Override
	public String executeImpl() {
		TypeDao td = new TypeDao(conn);
		CityDao cd = new CityDao(conn);

		types = td.list();
		cities = cd.list();

		return SUCCESS;
	}

	@Override
	public void validateImpl() {
	}
}
