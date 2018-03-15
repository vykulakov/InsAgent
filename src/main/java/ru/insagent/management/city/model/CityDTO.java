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

package ru.insagent.management.city.model;

import ru.insagent.model.City;

/**
 *
 */
public class CityDTO {
    private int id;
    private String name;
    private boolean removed;

    public CityDTO(City city) {
        this.id = city.getId();
        this.name = city.getName();
        this.removed = city.isRemoved();
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public boolean isRemoved() {
        return removed;
    }
}
