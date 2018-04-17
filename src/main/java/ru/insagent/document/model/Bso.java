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
import ru.insagent.model.User;
import ru.insagent.workflow.model.Node;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Abstract bso entity
 */
@MappedSuperclass
public abstract class Bso extends IdBase {
    private Date created;
    private String series;
    private long number;
    private boolean issued;
    private Date issuedDate;
    private boolean corrupted;
    private Date corruptedDate;
    private boolean registered;
    private Date registeredDate;
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

    @JSON(format = "dd.MM.yyyy HH:mm")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long l) {
        this.number = l;
    }

    public boolean isIssued() {
        return issued;
    }

    public void setIssued(boolean issued) {
        this.issued = issued;
    }

    @JSON(format = "dd.MM.yyyy HH:mm")
    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public User getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(User user) {
        this.issuedBy = user;
    }

    public Unit getIssuedUnit() {
        return issuedUnit;
    }

    public void setIssuedUnit(Unit issuedUnit) {
        this.issuedUnit = issuedUnit;
    }

    public boolean isCorrupted() {
        return corrupted;
    }

    public void setCorrupted(boolean corrupted) {
        this.corrupted = corrupted;
    }

    @JSON(format = "dd.MM.yyyy HH:mm")
    public Date getCorruptedDate() {
        return corruptedDate;
    }

    public void setCorruptedDate(Date corruptedDate) {
        this.corruptedDate = corruptedDate;
    }

    public User getCorruptedBy() {
        return corruptedBy;
    }

    public void setCorruptedBy(User corruptedBy) {
        this.corruptedBy = corruptedBy;
    }

    public Unit getCorruptedUnit() {
        return corruptedUnit;
    }

    public void setCorruptedUnit(Unit corruptedUnit) {
        this.corruptedUnit = corruptedUnit;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    @JSON(format = "dd.MM.yyyy HH:mm")
    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public User getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(User registeredBy) {
        this.registeredBy = registeredBy;
    }

    public Unit getRegisteredUnit() {
        return registeredUnit;
    }

    public void setRegisteredUnit(Unit registeredUnit) {
        this.registeredUnit = registeredUnit;
    }

    public String getInsured() {
        return insured;
    }

    public void setInsured(String insured) {
        this.insured = insured;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

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
