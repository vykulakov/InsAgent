package ru.insagent.management.user.action;

import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

import ru.insagent.action.BaseAction;
import ru.insagent.dao.UserDao;
import ru.insagent.model.User;

public class UpdateUserJsonAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private User user;
	public User getUser() {
		return user;
	}
	@RequiredFieldValidator(message = "Пользователь не передан.", shortCircuit = true)
	public void setUser(User user) {
		this.user = user;
	}

	private String confirm;
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	@Override
	@Validations(
		requiredFields = {
			@RequiredFieldValidator(fieldName = "user.id",      message = "Идентификатор пользователя не указан.", shortCircuit = true),
			@RequiredFieldValidator(fieldName = "user.unit.id", message = "Идентификатор подразделения не указан.", shortCircuit = true),
			@RequiredFieldValidator(fieldName = "user.roles",   message = "Роль пользователя не найдена.", shortCircuit = true)
		},
		requiredStrings = {
			@RequiredStringValidator(fieldName = "user.username",  message = "Логин пользователя не указан.", shortCircuit = true),
			@RequiredStringValidator(fieldName = "user.firstName", message = "Имя пользователя не указано.", shortCircuit = true),
			@RequiredStringValidator(fieldName = "user.lastName",  message = "Фамилия пользователя не указана.", shortCircuit = true),
		},
		conversionErrorFields = {
			@ConversionErrorFieldValidator(fieldName = "user.id",     message = "Невозможно преобразовать идентификатор пользователя.", shortCircuit = true),
			@ConversionErrorFieldValidator(fieldName = "user.unit.id", message = "Невозможно преобразовать идентификатор подразделения.", shortCircuit = true)
		},
		intRangeFields = {
			@IntRangeFieldValidator(fieldName = "user.id",      min = "0", message = "Идентификатор пользователя имеет недопустимое значение.", shortCircuit = true),
			@IntRangeFieldValidator(fieldName = "user.unit.id", min = "0", max = "255", message = "Идентификатор подразделения имеет недопустимое значение.", shortCircuit = true)
		},
		stringLengthFields = {
			@StringLengthFieldValidator(fieldName = "user.username",  maxLength = "255",  message = "Логин пользователя должен содержать менее 255 символов."),
			@StringLengthFieldValidator(fieldName = "user.firstName", maxLength = "255",  message = "Имя пользователя должно содержать менее 255 символов."),
			@StringLengthFieldValidator(fieldName = "user.lastName",  maxLength = "255",  message = "Фамилия пользователя должна содержать менее 255 символов."),
			@StringLengthFieldValidator(fieldName = "user.comment",   maxLength = "2048", message = "Комментарий к пользователю должен содержать менее 2048 символов."),
		}
	)
	public String executeImpl() {
		UserDao ud = new UserDao(conn);

		ud.update(user);

		return SUCCESS;
	}

	@Override
	public void validateImpl() {
		if(user != null && user.getId() == 0 && (user.getPassword() == null || user.getPassword().isEmpty())) {
			addFieldError("user.password", "Пароль пользователя не указан.");
			return;
		}
		if(user != null && user.getPassword() != null && !user.getPassword().isEmpty() && user.getPassword().length() < 6) {
			addFieldError("user.password", "Пароль слишком короткий.");
			return;
		}
		if(user != null && user.getPassword() != null && !user.getPassword().isEmpty() && (confirm == null || !confirm.equals(user.getPassword()))) {
			addFieldError("confirm", "Введённые пароли не совпадают.");
			return;
		}
	}
}
