package ru.insagent.document.action;

import java.util.Arrays;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import ru.insagent.action.BaseAction;
import ru.insagent.workflow.dao.ActionDao;
import ru.insagent.workflow.model.Action;

public class CheckBsoJsonAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private Integer nodeId;
	public Integer getNodeId() {
		return nodeId;
	}
	@RequiredFieldValidator(message = "Идентификатор узла не передан.", shortCircuit = true)
	@ConversionErrorFieldValidator(message = "Невозможно преобразовать идентификатор узла.", shortCircuit = true)
	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	private Integer actionId;
	public Integer getActionId() {
		return actionId;
	}
	@RequiredFieldValidator(message = "Идентификатор действия не передан.", shortCircuit = true)
	@ConversionErrorFieldValidator(message = "Невозможно преобразовать идентификатор действия.", shortCircuit = true)
	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	{
		ALLOW_ROLES = Arrays.asList("admin", "director", "manager", "sale");
		ALLOW_MSG = "У вас нет прав для проверки БСО перед выполнением действия.";
	}

	public String executeImpl() {
		ActionDao ad = new ActionDao(conn);

		boolean result = false;
		for(Action action : ad.listByUser(user)) {
			if(action.getId() == actionId) {
				result = true;
				break;
			}
		}
		if(!result) {
			addActionError("Вам запрещено выполнять данное действие.");
			return ERROR;
		}

		result = false;
		for(int id : ad.listNodeIdsByActionId(actionId)) {
			if(id == nodeId) {
				result = true;
				break;
			}
		}
		if(!result) {
			addActionError("Над БСО нельзя выполнить данное действие в данном узле.");
			return ERROR;
		}

		return SUCCESS;
	}

	@Override
	public void validateImpl() {
	}
}
