package ru.insagent.management.user.action;

import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import ru.insagent.action.BaseAction;
import ru.insagent.management.dao.UserDao;

public class RemoveUserJsonAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private Integer userId;
	public Integer getUserId() {
		return userId;
	}
	@RequiredFieldValidator(message = "Пользователь не передан.", shortCircuit = true)
	@ConversionErrorFieldValidator(message = "Невозможно преобразовать идентификатор пользователя.", shortCircuit = true)
	@IntRangeFieldValidator(message = "Идентификатор пользователя имеет недопустимое значение.", min = "1", shortCircuit = true)
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String executeImpl() {
		UserDao ud = new UserDao(conn);

		ud.remove(userId);

		return SUCCESS;
	}

	@Override
	public void validateImpl() {
	}
}
