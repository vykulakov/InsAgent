package ru.insagent.document.action;

import java.util.Arrays;
import java.util.List;

import ru.insagent.action.GetBaseAction;
import ru.insagent.document.dao.ActDao;
import ru.insagent.document.dao.ActPackDao;
import ru.insagent.document.model.Act;

public class ListActsJsonAction extends GetBaseAction<Act> {
	private static final long serialVersionUID = 1L;

	public List<Act> getRows() {
		return rows;
	}

	public Integer getTotal() {
		return total;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin", "director", "manager", "sale");
		ALLOW_MSG = "У вас нет прав для просмотра журнала актов";
	}

	@Override
	public String executeImpl() {
		dao = new ActDao(conn);

		rows = dao.listByUser(user, sort, order, limit, offset);
		total = dao.getCount();

		ActPackDao pd = new ActPackDao(conn);
		for(Act act : rows) {
			act.setPacks(pd.listByActId(act.getId()));
		}

		return SUCCESS;
	}
}
