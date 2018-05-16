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

import lombok.Getter;
import lombok.Setter;
import ru.insagent.model.IdBase;
import ru.insagent.model.Unit;
import ru.insagent.workflow.model.Node;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Act entity
 */
@Setter
@Getter
@Entity
@Table(name = "d_acts")
public class Act extends IdBase {
    private static final long serialVersionUID = 6963271182212532121L;

    private LocalDateTime created;
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
    private Node nodeFrom;

    @ManyToOne
    @JoinColumn(name = "nodeToId")
    private Node nodeTo;

    @ManyToOne
    @JoinColumn(name = "unitFromId")
    private Unit unitFrom;

    @ManyToOne
    @JoinColumn(name = "unitToId")
    private Unit unitTo;
}

