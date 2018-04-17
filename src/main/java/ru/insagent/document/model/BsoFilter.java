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

import ru.insagent.model.Filter;
import ru.insagent.model.Unit;
import ru.insagent.workflow.model.Node;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BsoFilter extends Filter {
    private static final long serialVersionUID = -3893689810214482026L;

    private String search;
    private String series;
    private Long numberFrom;
    private Long numberTo;
    private String insured;
    private BigDecimal premiumFrom;
    private BigDecimal premiumTo;
    private List<Node> nodes;
    private List<Unit> units;
    private Date createdFrom;
    private Date createdTo;
    private Date issuedFrom;
    private Date issuedTo;
    private Date corruptedFrom;
    private Date corruptedTo;
    private Date registeredFrom;
    private Date registeredTo;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public Long getNumberFrom() {
        return numberFrom;
    }

    public void setNumberFrom(Long numberFrom) {
        this.numberFrom = numberFrom;
    }

    public Long getNumberTo() {
        return numberTo;
    }

    public void setNumberTo(Long numberTo) {
        this.numberTo = numberTo;
    }

    public String getInsured() {
        return insured;
    }

    public void setInsured(String insured) {
        this.insured = insured;
    }

    public BigDecimal getPremiumFrom() {
        return premiumFrom;
    }

    public void setPremiumFrom(BigDecimal premiumFrom) {
        this.premiumFrom = premiumFrom;
    }

    public BigDecimal getPremiumTo() {
        return premiumTo;
    }

    public void setPremiumTo(BigDecimal premiumTo) {
        this.premiumTo = premiumTo;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public Date getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(Date createdFrom) {
        this.createdFrom = createdFrom;
    }

    public Date getCreatedTo() {
        return createdTo;
    }

    public void setCreatedTo(Date createdTo) {
        this.createdTo = createdTo;
    }

    public Date getIssuedFrom() {
        return issuedFrom;
    }

    public void setIssuedFrom(Date issuedFrom) {
        this.issuedFrom = issuedFrom;
    }

    public Date getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(Date issuedTo) {
        this.issuedTo = issuedTo;
    }

    public Date getCorruptedFrom() {
        return corruptedFrom;
    }

    public void setCorruptedFrom(Date corruptedFrom) {
        this.corruptedFrom = corruptedFrom;
    }

    public Date getCorruptedTo() {
        return corruptedTo;
    }

    public void setCorruptedTo(Date corruptedTo) {
        this.corruptedTo = corruptedTo;
    }

    public Date getRegisteredFrom() {
        return registeredFrom;
    }

    public void setRegisteredFrom(Date registeredFrom) {
        this.registeredFrom = registeredFrom;
    }

    public Date getRegisteredTo() {
        return registeredTo;
    }

    public void setRegisteredTo(Date registeredTo) {
        this.registeredTo = registeredTo;
    }

    @Override
    public String toString() {
        return "BsoFilter [series=" + series + ", numberFrom=" + numberFrom + ", numberTo=" + numberTo + ", insured=" + insured + ", premiumFrom=" + premiumFrom + ", premiumTo=" + premiumTo + ", nodes=" + nodes + ", units=" + units
                + ", createdFrom=" + createdFrom + ", createdTo=" + createdTo
                + ", issuedFrom=" + issuedFrom + ", issuedTo=" + issuedTo
                + ", corruptedFrom=" + corruptedFrom + ", corruptedTo=" + corruptedTo
                + ", registeredFrom=" + registeredFrom + ", registeredTo=" + registeredTo
                + "]";
    }
}
