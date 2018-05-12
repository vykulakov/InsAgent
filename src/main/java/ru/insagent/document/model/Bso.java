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
import ru.insagent.model.User;
import ru.insagent.workflow.model.Node;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Abstract bso entity
 */
@Setter
@Getter
@MappedSuperclass
public abstract class Bso extends IdBase {
    private LocalDateTime created;
    private String series;
    private long number;
    private boolean issued;
    private LocalDateTime issuedDate;
    private boolean corrupted;
    private LocalDateTime corruptedDate;
    private boolean registered;
    private LocalDateTime registeredDate;
    private String insured;
    private BigDecimal premium;

    @ManyToOne
    @JoinColumn(name = "unitId")
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "nodeId")
    private Node node;

    @ManyToOne
    @JoinColumn(name = "issuedBy")
    private User issuedBy;

    @ManyToOne
    @JoinColumn(name = "issuedUnitId")
    private Unit issuedUnit;

    @ManyToOne
    @JoinColumn(name = "corruptedBy")
    private User corruptedBy;

    @ManyToOne
    @JoinColumn(name = "corruptedUnitId")
    private Unit corruptedUnit;

    @ManyToOne
    @JoinColumn(name = "registeredBy")
    private User registeredBy;

    @ManyToOne
    @JoinColumn(name = "registeredUnitId")
    private Unit registeredUnit;

    public static void copy(Bso to, Bso from) {
        to.id = from.id;
        to.created = from.created;
        to.series = from.series;
        to.number = from.number;
        to.issued = from.issued;
        to.issuedDate = from.issuedDate;
        to.issuedBy = from.issuedBy;
        to.issuedUnit = from.issuedUnit;
        to.corrupted = from.corrupted;
        to.corruptedDate = from.corruptedDate;
        to.corruptedBy = from.corruptedBy;
        to.corruptedUnit = from.corruptedUnit;
        to.registered = from.registered;
        to.registeredDate = from.registeredDate;
        to.registeredBy = from.registeredBy;
        to.registeredUnit = from.registeredUnit;
        to.insured = from.insured;
        to.premium = from.premium;
        to.unit = from.unit;
        to.node = from.node;
    }
}
