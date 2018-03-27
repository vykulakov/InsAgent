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

package ru.insagent.management.user.model;

import ru.insagent.model.User;

/**
 *
 */
public class UserDTO {
    private int id;
    private String login;
    private String name;
    private int unitId;
    private String unitName;
    private boolean removed;

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getUsername();
        this.name = user.getFirstName() + " " + user.getLastName();
        this.removed = user.isRemoved();
        if (user.getUnit() != null) {
            this.unitId = user.getUnit().getId();
            this.unitName = user.getUnit().getName();
        }
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public int getUnitId() {
        return unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public boolean isRemoved() {
        return removed;
    }
}
