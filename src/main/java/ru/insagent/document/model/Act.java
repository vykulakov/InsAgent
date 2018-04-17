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

package ru.insagent.document.model;

import org.apache.struts2.json.annotations.JSON;
import ru.insagent.model.IdBase;
import ru.insagent.model.Unit;
import ru.insagent.workflow.model.Node;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Act entity
 */
@Entity
@Table(name = "d_acts")
public class Act extends IdBase {
    private static final long serialVersionUID = 6963271182212532121L;

    private Date created;
    private int amount;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "typeId")
    private ActType type;

    @OneToMany
    @JoinColumn(name = "actId")
    private List<ActPack> packs;

    @ManyToOne
    @JoinColumn(name = "nodeFromId")
    private Node NodeFrom;

    @ManyToOne
    @JoinColumn(name = "nodeToId")
    private Node NodeTo;

    @ManyToOne
    @JoinColumn(name = "unitFromId")
    private Unit UnitFrom;

    @ManyToOne
    @JoinColumn(name = "unitToId")
    private Unit UnitTo;

    public ActType getType() {
        return type;
    }

    public void setType(ActType type) {
        this.type = type;
    }

    @JSON(format = "dd.MM.yyyy HH:mm")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Node getNodeFrom() {
        return NodeFrom;
    }

    public void setNodeFrom(Node nodeFrom) {
        NodeFrom = nodeFrom;
    }

    public Node getNodeTo() {
        return NodeTo;
    }

    public void setNodeTo(Node nodeTo) {
        NodeTo = nodeTo;
    }

    public Unit getUnitFrom() {
        return UnitFrom;
    }

    public void setUnitFrom(Unit unitFrom) {
        UnitFrom = unitFrom;
    }

    public Unit getUnitTo() {
        return UnitTo;
    }

    public void setUnitTo(Unit unitTo) {
        UnitTo = unitTo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ActPack> getPacks() {
        return packs;
    }

    public void setPacks(List<ActPack> packs) {
        this.packs = packs;
    }

    @Override
    public String toString() {
        return "Act [id=" + id + ", type=" + type + ", created=" + created + ", NodeFrom=" + NodeFrom + ", NodeTo=" + NodeTo + ", UnitFrom=" + UnitFrom + ", UnitTo=" + UnitTo + ", comment=" + comment + ", packs=" + packs + "]";
    }
}
