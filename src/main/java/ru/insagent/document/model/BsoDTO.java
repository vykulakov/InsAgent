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

package ru.insagent.document.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.insagent.model.Unit;
import ru.insagent.model.User;
import ru.insagent.workflow.model.Node;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * BSO DTO.
 */
@Getter
@Setter
public class BsoDTO {
    private int id;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime created;
    private String series;
    private long number;
    private boolean issued;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime issuedDate;
    private boolean corrupted;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime corruptedDate;
    private boolean registered;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime registeredDate;
    private String insured;
    private BigDecimal premium;

    private int unitId;
    private String unitName;

    private int nodeId;
    private String nodeName;

    private int issuedById;
    private String issuedByName;

    private int issuedUnitId;
    private String issuedUnitName;

    private int corruptedById;
    private String corruptedByName;

    private int corruptedUnitId;
    private String corruptedUnitName;

    private int registeredById;
    private String registeredByName;

    private int registeredUnitId;
    private String registeredUnitName;

    public BsoDTO(Bso bso) {
        this.id = bso.getId();
        this.created = bso.getCreated();
        this.series = bso.getSeries();
        this.number = bso.getNumber();
        this.issued = bso.isIssued();
        this.issuedDate = bso.getIssuedDate();
        if (bso.getIssuedBy() != null) {
            this.issuedById = bso.getIssuedBy().getId();
            this.issuedByName = bso.getIssuedBy().getFirstName() + " " + bso.getIssuedBy().getLastName();
        }
        if (bso.getIssuedUnit() != null) {
            this.issuedUnitId = bso.getIssuedUnit().getId();
            this.issuedUnitName = bso.getIssuedUnit().getName();
        }
        this.corrupted = bso.isCorrupted();
        this.corruptedDate = bso.getCorruptedDate();
        if (bso.getCorruptedBy() != null) {
            this.corruptedById = bso.getCorruptedBy().getId();
            this.corruptedByName = bso.getCorruptedBy().getFirstName() + " " + bso.getCorruptedBy().getLastName();
        }
        if (bso.getCorruptedUnit() != null) {
            this.corruptedUnitId = bso.getCorruptedUnit().getId();
            this.corruptedUnitName = bso.getCorruptedUnit().getName();
        }
        this.registered = bso.isRegistered();
        this.registeredDate = bso.getRegisteredDate();
        if (bso.getRegisteredBy() != null) {
            this.registeredById = bso.getRegisteredBy().getId();
            this.registeredByName = bso.getRegisteredBy().getFirstName() + " " + bso.getRegisteredBy().getLastName();
        }
        if (bso.getRegisteredUnit() != null) {
            this.registeredUnitId = bso.getRegisteredUnit().getId();
            this.registeredUnitName = bso.getRegisteredUnit().getName();
        }
        this.insured = bso.getInsured();
        this.premium = bso.getPremium();
        if (bso.getUnit() != null) {
            this.unitId = bso.getUnit().getId();
            this.unitName = bso.getUnit().getName();
        }
        if (bso.getNode() != null) {
            this.nodeId = bso.getNode().getId();
            this.nodeName = bso.getNode().getName();
        }
    }
}
