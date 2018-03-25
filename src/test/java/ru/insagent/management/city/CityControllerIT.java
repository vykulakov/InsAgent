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

package ru.insagent.management.city;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import ru.insagent.management.ControllerIT;
import ru.insagent.model.City;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CityControllerIT extends ControllerIT {
    @Test
    public void city() {
        String body = executeWithBody("/management/city.html", null, HttpMethod.GET);

        assertThat(body).contains("<h2>Управление городами</h2>");
    }

    @Test
    public void get() {
        String body = executeWithBody("/management/city/1", null, HttpMethod.GET);

        ReadContext ctx = JsonPath.parse(body);
        City city = ctx.read("$", City.class);

        assertThat(city.getId()).isEqualTo(1);
        assertThat(city.getName()).isEqualTo("Воронеж");
    }

    @Test
    public void list() {
        String body = executeWithBody("/management/city", null, HttpMethod.GET);

        ReadContext ctx = JsonPath.parse(body);
        List rows = ctx.read("$.rows", List.class);
        Integer total = ctx.read("$.total", Integer.class);

        assertThat(total).isGreaterThan(0);
        assertThat(rows.size()).isGreaterThan(0);
    }

    @Test
    public void update() {
        final City city = createTestCity();

        try {
            LinkedMultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("id", String.valueOf(city.getId()));
            form.add("name", city.getName() + " TEST");
            form.add("comment", city.getComment() + " TEST");

            executeWithoutBody("/management/city", form, HttpMethod.POST);

            City c = getEntity(city.getClass(), city.getId());
            assertThat(c).isNotNull();
            assertThat(c.getName()).isEqualTo(city.getName() + " TEST");
            assertThat(c.getComment()).isEqualTo(city.getComment() + " TEST");
        } finally {
            removeEntity(city.getClass(), city.getId());
        }
    }

    @Test
    public void remove() {
        final City city = createTestCity();

        try {
            executeWithoutBody("/management/city/" + city.getId(), null, HttpMethod.DELETE);

            City c = getEntity(city.getClass(), city.getId());
            assertThat(c).isNotNull();
            assertThat(c.isRemoved()).isTrue();
        } finally {
            removeEntity(city.getClass(), city.getId());
        }
    }

    private City createTestCity() {
        City city = new City();
        city.setName("Name " + System.currentTimeMillis());
        city.setComment("Comment " + System.currentTimeMillis());

        createEntity(city);

        return city;
    }
}