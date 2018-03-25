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

package ru.insagent.management.unit;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import ru.insagent.management.ControllerIT;
import ru.insagent.model.City;
import ru.insagent.model.Unit;
import ru.insagent.model.UnitType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnitControllerIT extends ControllerIT {
    @Test
    public void unit() {
        String body = executeWithBody("/management/unit.html", null, HttpMethod.GET);

        assertThat(body).contains("<h2>Управление подразделениями</h2>");
    }

    @Test
    public void get() {
        String body = executeWithBody("/management/unit/2", null, HttpMethod.GET);

        ReadContext ctx = JsonPath.parse(body);
        Unit unit = ctx.read("$", Unit.class);

        assertThat(unit.getId()).isEqualTo(2);
        assertThat(unit.getName()).isEqualTo("Филиал Воронеж");
    }

    @Test
    public void list() {
        String body = executeWithBody("/management/unit", null, HttpMethod.GET);

        ReadContext ctx = JsonPath.parse(body);
        List rows = ctx.read("$.rows", List.class);
        Integer total = ctx.read("$.total", Integer.class);

        assertThat(total).isGreaterThan(0);
        assertThat(rows.size()).isGreaterThan(0);
    }

    @Test
    public void update() {
        final Unit unit = createTestUnit();

        try {
            LinkedMultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("id", String.valueOf(unit.getId()));
            form.add("name", unit.getName() + " TEST");
            form.add("city.id", "2");
            form.add("type.id", "2");
            form.add("comment", unit.getComment() + " TEST");

            executeWithoutBody("/management/unit", form, HttpMethod.POST);

            Unit u = getEntity(unit.getClass(), unit.getId());
            assertThat(u).isNotNull();
            assertThat(u.getName()).isEqualTo(unit.getName() + " TEST");
            assertThat(u.getComment()).isEqualTo(unit.getComment() + " TEST");
        } finally {
            removeEntity(unit.getClass(), unit.getId());
        }
    }

    @Test
    public void remove() {
        final Unit unit = createTestUnit();

        try {
            executeWithoutBody("/management/unit/" + unit.getId(), null, HttpMethod.DELETE);

            Unit u = getEntity(unit.getClass(), unit.getId());
            assertThat(u).isNotNull();
            assertThat(u.isRemoved()).isTrue();
        } finally {
            removeEntity(unit.getClass(), unit.getId());
        }
    }

    private Unit createTestUnit() {
        Unit unit = new Unit();
        unit.setName("Name " + System.currentTimeMillis());
        unit.setComment("Comment " + System.currentTimeMillis());

        unit.setCity(new City());
        unit.getCity().setId(1);

        unit.setType(new UnitType());
        unit.getType().setId(1);

        createEntity(unit);

        return unit;
    }
}