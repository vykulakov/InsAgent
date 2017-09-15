package ru.insagent.servlet.filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.insagent.exception.AppException;
import ru.insagent.management.model.User;
import ru.insagent.model.Menu;
import ru.insagent.util.JdbcUtils;
import ru.insagent.util.Setup;

public class AuthFilter implements Filter {
	private String forwardPage = "/jsp/main.jsp";

	private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

	@Override
	public void init(FilterConfig paramFilterConfig) throws ServletException {
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory();
		org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String path = request.getContextPath();
		String uri = request.getRequestURI().replaceFirst(path, "");

		Subject currentUser = SecurityUtils.getSubject();

		if(uri.equals("/logout")) {
			currentUser.logout();

			redirect(request, response, path);

			return;
		}

		if(currentUser.isAuthenticated()) {
			chain.doFilter(request, response);
			return;
		}

		if(uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/image/")) {
			chain.doFilter(request, response);
			return;
		}

		// Сохраняем адрес запроса, чтобы после авторизации сразу попасть на нужную страницу
		if(request.getQueryString() != null && !request.getQueryString().trim().isEmpty()) {
			request.setAttribute("authUrl", request.getRequestURI() + "?" + request.getQueryString());
		} else {
			request.setAttribute("authUrl", request.getRequestURI());
		}

		String auth = request.getParameter("authDo");
		String remember = request.getParameter("authRememberMe");
		String username = request.getParameter("authUser");
		String password = request.getParameter("authPass");
		if(auth != null && auth.equals("true")) {
			if((username == null || username.isEmpty()) && (password == null || password.isEmpty())) {
				request.setAttribute("authError", "Вы не указали логин и пароль");
				forward(request, response, forwardPage);
				return;
			}
			
			if(username == null || username.isEmpty()) {
				request.setAttribute("authError", "Вы не указали логин");
				forward(request, response, forwardPage);
				return;
			} else {
				request.setAttribute("authUser", username);
			}
	
			if(password == null || password.isEmpty()) {
				request.setAttribute("authError", "Вы не указали пароль");
				forward(request, response, forwardPage);
				return;
			}
		} else {
			forward(request, response, forwardPage);
			return;
		}

		boolean tokenRememberMe;
		if(remember != null && remember.equals("true")) {
			tokenRememberMe = true;
		} else {
			tokenRememberMe = false;
		}

		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		token.setRememberMe(tokenRememberMe);
		try {
		    currentUser.login(token);
		} catch (AuthenticationException ae) {
			request.setAttribute("authError", "Ошибка аутентификации");
		}

		if(currentUser.isAuthenticated()) {
			String lastIp = request.getHeader("X-Real-IP");
			if(lastIp == null) {
				lastIp = request.getRemoteAddr();
			}

			User user = (User) currentUser.getPrincipal();
			user.setLastIp(lastIp);
			user.setLastAuth(new Date());

			updateUser(user);

			// Получаем меню для авторизованного пользователя и сохраняем его в сессию.
			// Как видно, меню получается только один раз сразу после аутентификации пользователя.
			currentUser.getSession().setAttribute("menu", getUserMenu(user));
		} else {
			forward(request, response, forwardPage);
			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

	public void forward(HttpServletRequest request, HttpServletResponse response, String page) throws IOException {
		try {
			request.getRequestDispatcher(page).forward(request, response);
		} catch (ServletException e) {
			logger.error("Cannot forward request", e);

			response.sendError(500, "Cannot forward request: " + e.getMessage());
		}
	}

	public void redirect(HttpServletRequest request, HttpServletResponse response, String location) throws IOException {
		response.sendRedirect(location);
	}

	public void updateUser(User user) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = Setup.getInstance().getConnection();

			ps = conn.prepareStatement(""
					+ " UPDATE"
					+ "     m_users"
					+ " SET"
					+ "     lastIp = ?,"
					+ "     lastAuth = FROM_UNIXTIME(?)"
					+ " WHERE"
					+ "     id = ?");
			ps.setString(1, user.getLastIp());
			ps.setLong(2, user.getLastAuth().getTime()/1000);
			ps.setInt(3, user.getId());
			ps.execute();
		} catch(SQLException e) {
			logger.error("Cannot update user in DB", e);
		} finally {
            JdbcUtils.closeStatement(ps);
            JdbcUtils.closeConnection(conn);
		}
	}

	public List<Menu> getUserMenu(User user) throws AppException {
		List<Menu> menu = new ArrayList<Menu>();

		int index = 1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuilder query = new StringBuilder("");
			query.append(""
					+ " SELECT"
					+ "     m.id AS menuId,"
					+ "     m.level AS menuLevel,"
					+ "     m.name AS menuName,"
					+ "     m.action AS menuAction"
					+ " FROM"
					+ "     b_menu m,"
					+ "     m_roles r,"
					+ "     b_menu_roles l"
					+ " WHERE"
					+ "     m.id = l.menuId AND"
					+ "     r.id = l.roleId");
			if(user.getRoles() != null && !user.getRoles().isEmpty()) {
				query.append(" AND r.idx IN (");
				for(int i = 0; i < user.getRoles().size(); i++) {
					query.append("?");
					query.append(",");
				}
				query.setLength(query.length() - 1);
				query.append(")");
			}
			query.append(""
					+ " ORDER BY"
					+ "    m.order;");

			conn = Setup.getInstance().getConnection();
			ps = conn.prepareStatement(query.toString());
			if(user.getRoles() != null && !user.getRoles().isEmpty()) {
				for(String role : user.getRoles()) {
					ps.setString(index++, role);
				}
			}
			rs = ps.executeQuery();
			while(rs.next()) {
				Menu item = new Menu();
				item.setId(rs.getInt("menuId"));
				item.setLevel(rs.getInt("menuLevel"));
				item.setName(rs.getString("menuName"));
				item.setAction(rs.getString("menuAction"));

				menu.add(item);
			}
		} catch(SQLException e) {
			throw new AppException("Cannot get menu", e);
		} finally {
			JdbcUtils.closeConnection(conn);
			JdbcUtils.closeStatement(ps);
			JdbcUtils.closeResultSet(rs);
		}

		return menu;
	}
}
