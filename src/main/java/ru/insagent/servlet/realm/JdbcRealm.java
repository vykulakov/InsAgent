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

package ru.insagent.servlet.realm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.insagent.exception.AppException;
import ru.insagent.management.dao.UserDao;
import ru.insagent.management.model.User;
import ru.insagent.util.JdbcUtils;
import ru.insagent.util.Setup;

public class JdbcRealm extends AuthorizingRealm   {
	private DefaultPasswordService passwordService = new DefaultPasswordService();

	private final Logger logger = LoggerFactory.getLogger(JdbcRealm.class);

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
			conn = Setup.getConnection();

			UserDao ud = new UserDao(conn);

			user = ud.getByUsername(username);
			if(user == null || !passwordService.passwordsMatch(password, user.getPassword())) {
				user = null;
			}
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
			conn = Setup.getConnection();

			UserDao ud = new UserDao(conn);

			roles = ud.getRolesByUserId(user.getId());
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
