package ru.insagent.management.user.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.insagent.action.BaseAction;
import ru.insagent.management.dao.RoleDao;
import ru.insagent.management.dao.UnitDao;
import ru.insagent.management.model.Role;
import ru.insagent.management.model.Unit;

public class UserManagementAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	{
		ALLOW_ROLES = Arrays.asList("admin");
		ALLOW_MSG = "У вас нет прав для управления пользователями";
	}

	private List<Role> roles = new ArrayList<Role>();
	public List<Role> getRoles() {
		return roles;
	}

	private List<Unit> units = new ArrayList<Unit>();
	public List<Unit> getUnits() {
		return units;
	}

	@Override
	public String executeImpl() {
		RoleDao rd = new RoleDao(conn);
		UnitDao ud = new UnitDao(conn);

		roles = rd.list();
		units = ud.listByUser(user);

		return SUCCESS;
	}

	@Override
	public void validateImpl() {
	}
}
