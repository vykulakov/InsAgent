package ru.insagent.management.user.action;

import java.util.Arrays;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import ru.insagent.action.GetBaseAction;
import ru.insagent.management.dao.UserDao;
import ru.insagent.management.model.UserFilter;
import ru.insagent.model.User;

public class GetUsersJsonAction extends GetBaseAction<User> {
	private static final long serialVersionUID = 1L;

	private UserFilter filter;
	public void setFilter(UserFilter filter) {
		this.filter = filter;
	}
	@JSON(serialize = false)
	public UserFilter getFilter() {
		return filter;
	}

	public List<User> getRows() {
		return rows;
	}

	public Integer getTotal() {
		return total;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin");
		ALLOW_MSG = "У вас нет прав для просмотра списка пользователей";
	}

	@Override
	public String executeImpl() {
		dao = new UserDao(conn);

		if(filter == null) {
			rows = dao.listByUser(user, search, sort, order, limit, offset);
		} else {
			rows = dao.listByUser(user, filter, sort, order, limit, offset);
		}
		total = dao.getCount();

		return SUCCESS;
	}
}
