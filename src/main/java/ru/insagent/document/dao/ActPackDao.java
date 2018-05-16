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

package ru.insagent.document.dao;

import org.springframework.stereotype.Repository;
import ru.insagent.dao.SimpleHDao;
import ru.insagent.document.model.ActPack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ActPackDao extends SimpleHDao<ActPack> {
    {
        clazz = ActPack.class;

        countQueryPrefix = ""
                + " SELECT"
                + "     COUNT(*) AS count"
                + " FROM"
                + "     d_act_packs p"
                + " WHERE"
                + "     1 = 1";

        selectQueryPrefix = ""
                + " SELECT"
                + "     p.id AS packId,"
                + "     p.series AS packSeries,"
                + "     p.numberFrom AS packNumberFrom,"
                + "     p.numberTo AS packNumberTo,"
                + "     p.amount AS packAmount"
                + " FROM"
                + "     d_act_packs p"
                + " WHERE"
                + "     1 = 1";
    }

    public List<ActPack> listByActId(int actId) {
        String where = "p.actId = :actId";
        Map<String, Object> objects = new HashMap<>();
        objects.put("actId", actId);

        return listByWhere(where, objects, null, null, 0, 0);
    }
}
