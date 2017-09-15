package ru.insagent.servlet.realm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.tomcat.jdbc.pool.DataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.insagent.exception.AppException;
import ru.insagent.management.dao.UserDao;
import ru.insagent.management.model.User;
import ru.insagent.util.JdbcUtils;

public class JdbcRealm extends AuthorizingRealm   {
	private DataSource dataSource;

	private DefaultPasswordService passwordService = new DefaultPasswordService();

	private final Logger logger = LoggerFactory.getLogger(JdbcRealm.class);

	public JdbcRealm() {
		super();

		try {
			Context initContext = new InitialContext();
			dataSource = (DataSource) initContext.lookup("java:/comp/env/jdbc/InsAgentDB");
		} catch (NamingException e) {
			logger.error("Cannot init DataSource", e);
			throw new AuthenticationException("Cannot init DataSource");
		}
		if (dataSource == null) {
			logger.error("DataSource is null");
			throw new AuthenticationException("DataSource is null");
		}
	}
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();
		String password = String.valueOf(upToken.getPassword());		

		User user = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();

			UserDao ud = new UserDao(conn);

			user = ud.getByUsername(username);
			if(user == null || !passwordService.passwordsMatch(password, user.getPassword())) {
				user = null;
			}
		} catch(SQLException e) {
			logger.error("Cannot get connection to DB", e);
		} catch(AppException e) {
			logger.error("Cannot get connection to DB", e);
		} finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
            JdbcUtils.closeConnection(conn);
		}
		if(user == null) {
			throw new AuthenticationException();
		}

		Set<String> roles = getRoles(user);
		user.addRoles(roles);

		return new SimpleAuthenticationInfo(user, upToken.getPassword(), "JoxLocalRealm");
	}
		
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {	
		User user = (User) getAvailablePrincipal(principals);

		Set<String> roles = getRoles(user);

		return new SimpleAuthorizationInfo(roles);
	}

	public Set<String> getRoles(User user) {
		Set<String> roles = new LinkedHashSet<String>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();

			UserDao ud = new UserDao(conn);

			roles = ud.getRolesByUserId(user.getId());
		} catch(SQLException e) {
			logger.error("Cannot get connection to DB", e);
		} catch(AppException e) {
			logger.error("Cannot get user roles from DB", e);
		} finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
            JdbcUtils.closeConnection(conn);
		}

		return roles;
	}
}
