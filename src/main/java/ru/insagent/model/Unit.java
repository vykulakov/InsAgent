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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "m_units")
public class Unit extends IdBase {
	private static final long serialVersionUID = -6544474496483192296L;

	private String name;
	private String comment;
	private boolean removed;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cityId")
	private City city;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeId")
	private UnitType type;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UnitType getType() {
		return type;
	}
	public void setType(UnitType type) {
		this.type = type;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public boolean isRemoved() {
		return removed;
	}
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	@Override
	public String toString() {
		return "Unit [id=" + id + ", name=" + name + ", type=" + type + ", city=" + city + ", comment=" + comment + ", removed=" + removed + "]";
	}
}
