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

package ru.insagent.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

/**
 * User entity
 */
@Entity
@Table(name="m_users")
public class User extends IdBase {
	private static final long serialVersionUID = 4996317034959435687L;

	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String comment;
	private String lastIp;
	private Date lastAuth;
	private boolean removed;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unitId")
	private Unit unit;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name="m_user_roles",
		joinColumns=@JoinColumn(name="userId", referencedColumnName="id"),
		inverseJoinColumns=@JoinColumn(name="roleId", referencedColumnName="id")
		)
	private Set<Role> roles = new HashSet<Role>();

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@JSON(serialize = false)
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getLastIp() {
		return lastIp;
	}
	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}
	@JSON(format = "dd.MM.yyyy HH:mm")
	public Date getLastAuth() {
		return lastAuth;
	}
	public void setLastAuth(Date lastAuth) {
		this.lastAuth = lastAuth;
	}
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
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
		if(roles == null) {
			return true;
		}

		return this.roles.containsAll(roles);
	}
	public Set<String> getRoles() {
		return roles;
	}
	public void addRole(String role) {
		this.roles.add(role);
	}
	public void addRoles(Collection<String> roles) {
		this.roles.addAll(roles);
	}
	public void setRoles(Collection<String> roles) {
		this.roles.clear();
		this.roles.addAll(roles);
	}
	public boolean isRemoved() {
		return removed;
	}
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName + ", lastName=" + lastName + ", comment=" + comment + ", lastIp=" + lastIp
			+ ", lastAuth=" + lastAuth + ", unit=" + unit + ", roles=" + roles + ", removed=" + removed + "]";
	}
}
