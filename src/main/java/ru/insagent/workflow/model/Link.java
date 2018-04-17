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

import ru.insagent.document.model.ActType;
import ru.insagent.model.IdBase;
import ru.insagent.model.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Link entity
 */
@Entity
@Table(name = "w_links")
public class Link extends IdBase {
    private static final long serialVersionUID = -4192191224685061836L;

    private String name;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nodeFromId")
    private Node nodeFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nodeToId")
    private Node nodeTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actTypeId")
    private ActType actType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "w_link_roles",
            joinColumns = @JoinColumn(name = "linkId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "id")
    )
    private List<Role> roles = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getNodeFrom() {
        return nodeFrom;
    }

    public void setNodeFrom(Node nodeFrom) {
        this.nodeFrom = nodeFrom;
    }

    public Node getNodeTo() {
        return nodeTo;
    }

    public void setNodeTo(Node nodeTo) {
        this.nodeTo = nodeTo;
    }

    public ActType getActType() {
        return actType;
    }

    public void setActType(ActType actType) {
        this.actType = actType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Link{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nodeFrom=" + nodeFrom +
                ", nodeTo=" + nodeTo +
                ", actType=" + actType +
                ", comment='" + comment + '\'' +
                ", roles=" + roles +
                '}';
    }
}
