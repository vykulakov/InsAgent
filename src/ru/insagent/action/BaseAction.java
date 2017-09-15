package ru.insagent.action;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.insagent.exception.AppException;
import ru.insagent.management.model.User;
import ru.insagent.util.JdbcUtils;
import ru.insagent.util.Setup;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public abstract class BaseAction extends ActionSupport implements Preparable {
	private static final long serialVersionUID = 1L;

	protected Connection conn;
	protected User user;

	/**
	 * Список ролей, которым разрешено выполнение данного действия
	 */
	protected List<String> ALLOW_ROLES;
	/**
	 * Сообщение пользователю, если у него нет прав для на выполнение данного действия
	 */
	protected String ALLOW_MSG = "У вас нет прав для выполнения данного действия";

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Пустой метод для получения фиктивной переменной из запроса,
	 * отвечающей за отключение кеширования.
	 * @param _
	 */
	public void set_(String _) {
	}

	public void prepare() {
		conn = Setup.getInstance().getConnection();
		if(conn == null) {
			logger.error("Bad connection");
		}

		user = (User) SecurityUtils.getSubject().getPrincipal();
		if(user == null) {
			logger.error("Bad user");
		}
	}

	public String execute() {
		try {
			return executeImpl();
		} catch(AppException e) {
			logger.error("Cannot execute action", e);
			addActionError(e.getMessage());
			return ERROR;
		} finally {
			JdbcUtils.closeConnection(conn);
		}
	}

	public abstract String executeImpl() throws AppException;

	public void validate() {
		hackFieldErrors();

		try {
			if(!user.hasRolesOne(ALLOW_ROLES)) {
				addActionError(ALLOW_MSG);
				return;
			}

			validateImpl();
		} finally {
			if(hasErrors()) {
				JdbcUtils.closeConnection(conn);
			}
		}
	}

	public abstract void validateImpl();

	/**
	 * <p>Удаление ошибки "Invalid field value for field"</p>
	 * <p>Метод очень плохой, так делать нельзя!!!</p>
	 * <p>В будущем нужно заменить на нормальную настройку Struts2</p>
	 */
	public void hackFieldErrors() {
		Map<String, List<String>> oldErrors = getFieldErrors();
		Map<String, List<String>> newErrors = new HashMap<String, List<String>>();

		for(Entry<String, List<String>> entry : oldErrors.entrySet()) {
			String key = entry.getKey();
			List<String> oldValues = entry.getValue();
			List<String> newValues = new LinkedList<String>();

			for(String value : oldValues) {
				if(value.startsWith("Invalid field value for field")) {
					continue;
				}

				newValues.add(value);
			}
			newErrors.put(key, newValues);
		}
		setFieldErrors(newErrors);
	}
}
