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

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "m_cities")
public class City extends IdBase {
	private static final long serialVersionUID = -4521788722648020946L;

	private String name;
	private String comment;
	private boolean removed;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		return "City [id=" + id + ", name=" + name + ", comment=" + comment + ", removed=" + removed + "]";
	}

    public static City makeEditableCopy(City orig) {
        City city = new City();
        city.id = orig.id;
        city.name = orig.name;
        city.comment = orig.comment;
        city.removed = orig.removed;

        return city;
    }
}
