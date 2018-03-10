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

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Authorized user
 */
public class SpringUser extends org.springframework.security.core.userdetails.User {
    private int id;
    private String name;
    private List<MenuItem> items;

    public SpringUser(User user, List<MenuItem> items, List<GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);

        this.id = user.getId();
        this.name = String.format("%s %s", user.getFirstName(), user.getLastName());
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<MenuItem> getItems() {
        return items;
    }
}
