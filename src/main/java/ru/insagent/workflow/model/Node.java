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

package ru.insagent.workflow.model;

import ru.insagent.model.IdBase;
import ru.insagent.model.Unit;
import ru.insagent.model.UnitType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Node entity
 */
@Entity
@Table(name = "w_nodes")
public class Node extends IdBase {
    private static final long serialVersionUID = -9054275807494235590L;

    private String name;

    @ManyToOne
    @JoinColumn(name = "nodeTypeId")
    private NodeType nodeType;

    @ManyToOne
    @JoinColumn(name = "unitTypeId")
    private UnitType unitType;

    @Transient
    private List<Unit> units = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nodeType=" + nodeType +
                ", unitType=" + unitType +
                ", units=" + units +
                '}';
    }
}
