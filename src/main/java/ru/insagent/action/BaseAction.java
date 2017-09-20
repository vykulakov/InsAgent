/*
 * InsAgent - https://github.com/vykulakov/InsAgent
 *
 * Copyright 2017 Vyacheslav Kulakov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.insagent.action;

import java.sql.Connection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import ru.insagent.exception.AppException;
import ru.insagent.model.User;
import ru.insagent.util.Hibernate;
import ru.insagent.util.JdbcUtils;
import ru.insagent.util.Setup;

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
	 * @param empty
	 */
	public void set_(String empty) {
	}

	@Override
	public void prepare() {
		conn = Setup.getConnection();
		if(conn == null) {
			logger.error("Bad connection");
		}

		user = (User) SecurityUtils.getSubject().getPrincipal();
		if(user == null) {
			logger.error("Bad user");
		}
	}

	@Override
	public String execute() {
		Session session = Hibernate.getCurrentSession();
		session.beginTransaction();
		try {
			String result = executeImpl();
			if(result == SUCCESS) {
				session.getTransaction().commit();
			} else {
				session.getTransaction().rollback();
			}
			return result;
		} catch(AppException e) {
			addActionError(e.getMessage());
			logger.error("Cannot execute action", e);
			session.getTransaction().rollback();
			return ERROR;
		} finally {
			JdbcUtils.closeConnection(conn);
		}
	}

	public String executeImpl() throws AppException {
		return SUCCESS;
	}

	@Override
	public void validate() {
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
}
