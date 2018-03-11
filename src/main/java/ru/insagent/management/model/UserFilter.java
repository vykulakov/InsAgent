/*
 * InsAgent - https://github.com/vykulakov/InsAgent
 *
 * Copyright 2017-2018 Vyacheslav Kulakov
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

package ru.insagent.management.model;

import ru.insagent.model.Filter;
import ru.insagent.model.Unit;

import java.util.List;

public class UserFilter extends Filter {
    private String name;
    private String login;
    private String search;
    private List<Unit> units;
    private Boolean removed;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getSearch() {
        return search;
    }
    public void setSearch(String search) {
        this.search = search;
    }
    public List<Unit> getUnits() {
        return units;
    }
    public void setUnits(List<Unit> units) {
        this.units = units;
    }
    public boolean getRemoved() {
        return removed;
    }
    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
