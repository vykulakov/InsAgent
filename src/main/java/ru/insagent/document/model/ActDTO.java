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

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.json.annotations.JSON;
import ru.insagent.model.IdBase;
import ru.insagent.model.Unit;
import ru.insagent.workflow.model.Node;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Act DTO.
 */
@Getter
@Setter
public class ActDTO {
    private int id;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime created;
    private int amount;
    private String comment;

    private int typeId;
    private String typeName;

    private int nodeFromId;
    private String nodeFromName;

    private int nodeToId;
    private String nodeToName;

    private int unitFromId;
    private String unitFromName;

    private int unitToId;
    private String unitToName;

    public ActDTO(Act act) {
        this.id = act.getId();
        this.created = act.getCreated();
        this.amount = act.getAmount();
        this.comment = act.getComment();
        if (act.getType() != null) {
            this.typeId = act.getType().getId();
            this.typeName = act.getType().getFullName();
        }
        if (act.getNodeFrom() != null) {
            this.nodeFromId = act.getNodeFrom().getId();
            this.nodeFromName = act.getNodeFrom().getName();
        }
        if (act.getNodeTo() != null) {
            this.nodeToId = act.getNodeTo().getId();
            this.nodeToName = act.getNodeTo().getName();
        }
        if (act.getUnitFrom() != null) {
            this.unitFromId = act.getUnitFrom().getId();
            this.unitFromName = act.getUnitFrom().getName();
        }
        if (act.getUnitTo() != null) {
            this.unitToId = act.getUnitTo().getId();
            this.unitToName = act.getUnitTo().getName();
        }
    }
}
