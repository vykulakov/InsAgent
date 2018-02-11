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

import java.util.Objects;
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

import ru.insagent.model.ShiroUser;
import ru.insagent.service.UserService;

public class JdbcRealm extends AuthorizingRealm {
	private DefaultPasswordService passwordService = new DefaultPasswordService();

	private final static Logger logger = LoggerFactory.getLogger(JdbcRealm.class);

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();
		char[] password = upToken.getPassword();

		try {
			ShiroUser shiroUser = new UserService().getByUsername(username);
			if (shiroUser == null || !passwordService.passwordsMatch(password, shiroUser.getPassword())) {
				logger.info("Password verification failed");
				throw new AuthenticationException();
			}

			return new SimpleAuthenticationInfo(shiroUser, upToken.getPassword(), "JoxLocalRealm");
		} catch (AuthenticationException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Cannot get connection to DB", e);
			throw new AuthenticationException();
		}
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser authUser = (ShiroUser) getAvailablePrincipal(principals);

		Set<String> roles = authUser.getRoles();

		return new SimpleAuthorizationInfo(roles);
	}
}
