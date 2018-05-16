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
import ru.insagent.model.Filter;
import ru.insagent.model.Unit;
import ru.insagent.workflow.model.Node;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
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
}
