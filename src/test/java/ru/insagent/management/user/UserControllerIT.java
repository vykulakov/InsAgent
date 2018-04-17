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

package ru.insagent.management.user;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import ru.insagent.ControllerIT;
import ru.insagent.model.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT extends ControllerIT {
    @Test
    public void user() {
        String body = executeWithBody("/management/user.html", null, HttpMethod.GET);

        assertThat(body).contains("<h2>Управление пользователями</h2>");
    }

    @Test
    public void get() {
        String body = executeWithBody("/management/user/1", null, HttpMethod.GET);

        ReadContext ctx = JsonPath.parse(body);
        User user = ctx.read("$", User.class);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isEqualTo("vkulakov");
    }

    @Test
    public void list() {
        String body = executeWithBody("/management/user", null, HttpMethod.GET);

        ReadContext ctx = JsonPath.parse(body);
        List rows = ctx.read("$.rows", List.class);
        Integer total = ctx.read("$.total", Integer.class);

        assertThat(total).isGreaterThan(0);
        assertThat(rows.size()).isGreaterThan(0);
    }

    @Test
    public void update() {
        final User user = createTestUser();

        try {
            LinkedMultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("id", String.valueOf(user.getId()));
            form.add("username", user.getUsername() + " TEST");
            form.add("password", user.getPassword() + " TEST");
            form.add("firstName", user.getFirstName() + " TEST");
            form.add("lastName", user.getLastName() + " TEST");
            form.add("unit.id", "1");
            form.add("comment", user.getComment() + " TEST");

            executeWithoutBody("/management/user", form, HttpMethod.POST);

            User u = getEntity(user.getClass(), user.getId());
            assertThat(u).isNotNull();
            assertThat(u.getUsername()).isEqualTo(user.getUsername() + " TEST");
            assertThat(u.getFirstName()).isEqualTo(user.getFirstName() + " TEST");
            assertThat(u.getLastName()).isEqualTo(user.getLastName() + " TEST");
            assertThat(u.getComment()).isEqualTo(user.getComment() + " TEST");
        } finally {
            removeEntity(user.getClass(), user.getId());
        }
    }

    @Test
    public void remove() {
        final User user = createTestUser();

        try {
            executeWithoutBody("/management/user/" + user.getId(), null, HttpMethod.DELETE);

            User u = getEntity(user.getClass(), user.getId());
            assertThat(u).isNotNull();
            assertThat(u.isRemoved()).isTrue();
        } finally {
            removeEntity(user.getClass(), user.getId());
        }
    }

    private User createTestUser() {
        User user = new User();
        user.setUsername("Username " + System.currentTimeMillis());
        user.setPassword("Password " + System.currentTimeMillis());
        user.setLastName("Last name " + System.currentTimeMillis());
        user.setFirstName("First name " + System.currentTimeMillis());
        user.setComment("Comment " + System.currentTimeMillis());

        user.setUnit(new Unit());
        user.getUnit().setId(1);

        createEntity(user);

        return user;
    }
}