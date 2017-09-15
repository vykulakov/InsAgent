package ru.insagent.management.unit.action;

import java.util.Arrays;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import ru.insagent.action.GetBaseAction;
import ru.insagent.management.dao.UnitDao;
import ru.insagent.management.model.Unit;
import ru.insagent.management.model.UnitFilter;

public class GetUnitsJsonAction extends GetBaseAction<Unit> {
	private static final long serialVersionUID = 1L;

	private UnitFilter filter;
	public void setFilter(UnitFilter filter) {
		this.filter = filter;
	}
	@JSON(serialize = false)
	public UnitFilter getFilter() {
		return filter;
	}

	public List<Unit> getRows() {
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
		dao = new UnitDao(conn);

		if(filter == null) {
			rows = dao.listByUser(user, search, sort, order, limit, offset);
		} else {
			rows = dao.listByUser(user, filter, sort, order, limit, offset);
		}
		total = dao.getCount();

		return SUCCESS;
	}
}
