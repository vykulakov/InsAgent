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

package ru.insagent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.insagent.document.dao.ActDao;
import ru.insagent.document.dao.ActPackDao;
import ru.insagent.document.dao.BsoArchivedDao;
import ru.insagent.document.dao.BsoNormalDao;
import ru.insagent.document.model.*;
import ru.insagent.exception.NotFoundException;
import ru.insagent.model.Roles;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActService {
    private ActDao actDao;
    private BsoNormalDao bsoDao;
    private BsoArchivedDao bsoArchivedDao;

    @Autowired
    public void setActDao(ActDao actDao) {
        this.actDao = actDao;
    }

    /**
     * Get rows count for the last query.
     *
     * @return Rows count for the last query.
     */
    public Long getCount() {
        return actDao.getCount();
    }

    public List<ActDTO> list(Roles roles, ActFilter filter, String sortBy, String sortDir, int limitRows, int limitOffset) {
        return actDao
                .list(roles, filter, sortBy, sortDir, limitRows, limitOffset)
                .stream().map(ActDTO::new)
                .collect(Collectors.toList());
    }

    public void update(Act newAct) {
        if (newAct.getId() == 0) {
            actDao.add(newAct);

            for (ActPack pack : newAct.getPacks()) {
                for (long number = pack.getNumberFrom(); number <= pack.getNumberTo(); number++) {
                    BsoNormal bso = bsoDao.get(pack.getSeries(), number);
                    if (bso == null) {
                        bso = new BsoNormal();
                        bso.setUnit(newAct.getUnitTo());
                        bso.setNode(newAct.getNodeTo());
                        bso.setNumber(number);
                        bso.setSeries(pack.getSeries());
                    } else {
                        bso.setUnit(newAct.getUnitTo());
                        bso.setNode(newAct.getNodeTo());
                    }
                }
            }

            if (newAct.getType().getId() == 3) {
                newAct.getPacks().forEach(pack -> {
                    List<BsoNormal> bsos = bsoDao.listBySeriesAndNumbers(pack.getSeries(), pack.getNumberFrom(), pack.getNumberTo());
                    bsos.forEach(bso -> {
                        bsoDao.remove(bso);
                        bsoArchivedDao.add(new BsoArchived(bso));
                    });
                });
            }
        } else {
            Act oldAct = actDao.get(newAct.getId());
            if (oldAct == null) {
                throw new NotFoundException("Act not found");
            }

            oldAct.setComment(newAct.getComment());
        }
    }
}
