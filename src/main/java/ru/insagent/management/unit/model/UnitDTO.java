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

package ru.insagent.management.unit.model;

import ru.insagent.model.Unit;

/**
 *
 */
public class UnitDTO {
    private int id;
    private String name;
    private int cityId;
    private String cityName;
    private int typeId;
    private String typeName;
    private String comment;
    private boolean removed;

    public UnitDTO(Unit unit) {
        this.id = unit.getId();
        this.name = unit.getName();
        this.cityId = unit.getCity().getId();
        this.cityName = unit.getCity().getName();
        this.typeId = unit.getType().getId();
        this.typeName = unit.getType().getName();
        this.removed = unit.isRemoved();
        this.comment = unit.getComment();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getComment() {
        return comment;
    }

    public boolean isRemoved() {
        return removed;
    }
}
