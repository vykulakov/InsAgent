/*
 * InsAgent - https://github.com/vykulakov/InsAgent
 *
 * Copyright 2018 Vyacheslav Kulakov
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

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.insagent.exception.AppException;
import ru.insagent.model.ShiroUser;
import ru.insagent.model.User;
import ru.insagent.service.UserService;

import java.sql.Connection;
import java.util.List;

public abstract class BaseAction extends ActionSupport implements Preparable {
    private static final long serialVersionUID = 1L;

    @Deprecated
    protected Connection conn;

    protected User baseUser;
    protected ShiroUser shiroUser;

    /**
     * Список ролей, которым разрешено выполнение данного действия
     */
    protected List<String> ALLOW_ROLES;
    /**
     * Сообщение пользователю, если у него нет прав для на выполнение данного действия
     */
    protected String ALLOW_MSG = "У вас нет прав для выполнения данного действия";

    protected final static Logger logger = LoggerFactory.getLogger(BaseAction.class);

    /**
     * Пустой метод для получения фиктивной переменной из запроса,
     * отвечающей за отключение кеширования.
     */
    public void set_(String ignore) {
    }

    @Override
    public void prepare() {
        shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (shiroUser == null) {
            logger.error("Bad user");
        } else {
            baseUser = new UserService().get(shiroUser.getId());
        }
    }

    @Override
    public String execute() {
        try {
            return executeImpl();
        } catch (AppException e) {
            addActionError(e.getMessage());
            logger.error("Cannot execute action", e);
            return ERROR;
        }
    }

    public String executeImpl() throws AppException {
        return SUCCESS;
    }
}
