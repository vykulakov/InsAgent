package ru.insagent.management.city.action;

import java.util.Arrays;

import ru.insagent.action.BaseAction;

public class CityManagementAction extends BaseAction {
	private static final long serialVersionUID = 5659376617098691474L;

	{
		ALLOW_ROLES = Arrays.asList("admin");
		ALLOW_MSG = "У вас нет прав для управления городами";
	}
}
