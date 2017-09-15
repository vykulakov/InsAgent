package ru.insagent.document.action;

import java.util.Arrays;
import ru.insagent.action.BaseAction;

public class BsoArchivedJournalAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	{
		ALLOW_ROLES = Arrays.asList("admin", "director", "manager", "register");
		ALLOW_MSG = "У вас нет прав для работы с архивом БСО";
	}

	public String executeImpl() {
		return SUCCESS;
	}

	@Override
	public void validateImpl() {
	}
}
