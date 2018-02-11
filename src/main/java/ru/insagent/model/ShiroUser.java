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

package ru.insagent.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Authorized user
 */
public class ShiroUser extends IdBase {
	private static final long serialVersionUID = 4996317034959435687L;

	private String username;
	private String password;
	private String firstName;
	private String lastName;

	private Set<String> roles = new HashSet<>();

	private ShiroUser() {

	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	public boolean hasRole(String role) {
		return roles.contains(role);
	}
	public boolean hasRolesOne(Collection<String> roles) {
		if(roles == null) {
			return true;
		}

		for(String role : roles) {
			if(this.roles.contains(role)) {
				return true;
			}
		}

		return false;
	}
	public boolean hasRolesAll(Collection<String> roles) {
		return roles == null || this.roles.containsAll(roles);
	}

	@Override
	public String toString() {
		return "ShiroUser [username=" + username + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}

	public static ShiroUser of(User user) {
		ShiroUser shiroUser = new ShiroUser();
		shiroUser.setId(user.getId());
		shiroUser.setUsername(user.getUsername());
		shiroUser.setPassword(user.getPassword());
		shiroUser.setFirstName(user.getFirstName());
		shiroUser.setLastName(user.getLastName());
		shiroUser.setRoles(user.getRoles().stream().map(Role::getIdx).collect(Collectors.toSet()));
		return shiroUser;
	}
}
