package ru.insagent.management.city.action;

import java.util.Arrays;
import ru.insagent.action.BaseAction;

public class CityManagementAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	{
		ALLOW_ROLES = Arrays.asList("admin");
		ALLOW_MSG = "У вас нет прав для управления городами";
	}

	@Override
	public String executeImpl() {
		return SUCCESS;
	}

	@Override
	public void validateImpl() {
	}
}
