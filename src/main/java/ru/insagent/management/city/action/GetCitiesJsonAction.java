package ru.insagent.management.city.action;

import java.util.Arrays;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import ru.insagent.action.GetBaseAction;
import ru.insagent.management.dao.CityDao;
import ru.insagent.management.model.City;
import ru.insagent.management.model.CityFilter;

public class GetCitiesJsonAction extends GetBaseAction<City> {
	private static final long serialVersionUID = 1L;

	private CityFilter filter;
	public void setFilter(CityFilter filter) {
		this.filter = filter;
	}
	@JSON(serialize = false)
	public CityFilter getFilter() {
		return filter;
	}

	public List<City> getRows() {
		return rows;
	}

	public Integer getTotal() {
		return total;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin");
		ALLOW_MSG = "У вас нет прав для просмотра списка подразделений";
	}

	@Override
	public String executeImpl() {
		dao = new CityDao(conn);

		if(filter == null) {
			rows = dao.listByUser(user, search, sort, order, limit, offset);
		} else {
			rows = dao.listByUser(user, filter, sort, order, limit, offset);
		}
		total = dao.getCount();

		return SUCCESS;
	}
}
