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

import ru.insagent.model.IdBase;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Act pack entity
 */
@Entity
@Table(name = "d_act_packs")
public class ActPack extends IdBase {
    private static final long serialVersionUID = 3681691887322137171L;

    private String series;
    private long numberFrom;
    private long numberTo;
    private int amount;


    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public long getNumberFrom() {
        return numberFrom;
    }

    public void setNumberFrom(long numberFrom) {
        this.numberFrom = numberFrom;
    }

    public long getNumberTo() {
        return numberTo;
    }

    public void setNumberTo(long numberTo) {
        this.numberTo = numberTo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ActPack [id=" + id + ", series=" + series + ", numberFrom=" + numberFrom + ", numberTo=" + numberTo + ", amount=" + amount + "]";
    }
}
