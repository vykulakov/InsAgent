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

package ru.insagent.servlet.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.insagent.dao.MenuItemDao;
import ru.insagent.exception.AppException;
import ru.insagent.model.MenuItem;
import ru.insagent.model.ShiroUser;
import ru.insagent.util.Hibernate;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Authorization filter
 */
public class AuthFilter implements Filter {
	private static String contextPath;
	private static final String MAIN_PAGE = "/WEB-INF/jsp/main.jsp";

	private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		contextPath = filterConfig.getServletContext().getContextPath();

		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory();
		org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String uri = request.getRequestURI().replaceFirst(contextPath, "");

		Subject currentUser = SecurityUtils.getSubject();

		if (uri.equals("/logout")) {
			currentUser.logout();

			redirectToMainUri(response);

			return;
		}

		if (currentUser.isAuthenticated()) {
			chain.doFilter(request, response);
			return;
		}

		if (uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/image/")) {
			chain.doFilter(request, response);
			return;
		}

		// Сохраняем адрес запроса, чтобы после авторизации сразу попасть на нужную страницу
		if (request.getQueryString() != null && !request.getQueryString().trim().isEmpty()) {
			request.setAttribute("authUrl", request.getRequestURI() + "?" + request.getQueryString());
		} else {
			request.setAttribute("authUrl", request.getRequestURI());
		}

		String auth = request.getParameter("authDo");
		String username = request.getParameter("authUser");
		String password = request.getParameter("authPass");
		String remember = request.getParameter("authRememberMe");
		boolean tokenRememberMe = "true".equals(remember);
		if ("true".equals(auth)) {
			if ((username == null || username.isEmpty()) && (password == null || password.isEmpty())) {
				request.setAttribute("authError", "Вы не указали логин и пароль");
				forwardToMainPage(request, response);
				return;
			}

			if (username == null || username.isEmpty()) {
				request.setAttribute("authError", "Вы не указали логин");
				forwardToMainPage(request, response);
				return;
			} else {
				request.setAttribute("authUser", username);
			}

			if (password == null || password.isEmpty()) {
				request.setAttribute("authError", "Вы не указали пароль");
				forwardToMainPage(request, response);
				return;
			}
		} else {
			forwardToMainPage(request, response);
			return;
		}

		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		token.setRememberMe(tokenRememberMe);
		try {
			currentUser.login(token);
		} catch (AuthenticationException ae) {
			request.setAttribute("authError", "Ошибка аутентификации");
		}

		if (currentUser.isAuthenticated()) {
			String lastIp = request.getHeader("X-Real-IP");
			if (lastIp == null) {
				lastIp = request.getRemoteAddr();
			}

			Session session = Hibernate.getCurrentSession();
			session.beginTransaction();
			try {
				ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipal();

				currentUser.getSession().setAttribute("menu", getUserMenu(shiroUser));

				Hibernate.commit(session);
			} catch (AppException e) {
				Hibernate.rollback(session);
				logger.error("Cannot load or store entities", e);
			}
		} else {
			forwardToMainPage(request, response);
			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

	private void forwardToMainPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			request.getRequestDispatcher(MAIN_PAGE).forward(request, response);
		} catch (ServletException e) {
			logger.error("Cannot forward request", e);

			response.sendError(500, "Internal server error");
		}
	}

	private void redirectToMainUri(HttpServletResponse response) throws IOException {
		response.sendRedirect(contextPath);
	}

	private List<MenuItem> getUserMenu(ShiroUser shiroUser) throws AppException {
		if (shiroUser.getRoles() == null || shiroUser.getRoles().isEmpty()) {
			return Collections.emptyList();
		}

		try {
			return new MenuItemDao().listByRoleIdxes(shiroUser.getRoles());
		} catch (Exception e) {
			throw new AppException("Cannot get menu", e);
		}
	}
}
