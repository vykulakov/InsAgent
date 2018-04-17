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

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Archived bso entity
 */
@Entity
@Table(name = "d_bsos_archived")
public class BsoArchived extends Bso {
    private static final long serialVersionUID = -3893689812214482027L;

    public BsoArchived() {
    }

    public BsoArchived(BsoNormal bso) {
        super();
        BsoNormal.copy(this, bso);
    }

    @Override
    public String toString() {
        return "BsoArchived [id=" + id + ", series=" + getSeries() + ", number=" + getNumber() + ", node=" + getNode() + ", unit=" + getUnit() + ", issued=" + isIssued() + ", corrupted=" + isCorrupted() + ", registered=" + isRegistered() + "]";
    }
}
