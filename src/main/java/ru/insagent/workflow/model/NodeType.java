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

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Node type entity
 */
@Entity
@Table(name = "w_node_types")
public class NodeType extends IdBase {
    private static final long serialVersionUID = -9054275807494235590L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Node: id=");
        sb.append(id);
        sb.append("; name=");
        sb.append(name);

        return sb.toString();
    }
}
