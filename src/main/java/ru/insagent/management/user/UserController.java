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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.insagent.management.user.model.UserFilter;
import ru.insagent.model.Response;
import ru.insagent.service.UserService;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/management/user")
    public String user(Model model) {
        model.addAttribute("roles", userService.roles());
        model.addAttribute("units", userService.units());

        return "management/user";
    }

    @ResponseBody
    @GetMapping("/management/users")
    public Response users(
            UserFilter filter,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset
    ) {
        Response response = new Response();
        response.setRows(userService.listByUser(null, filter, sort, order, limit, offset));
        response.setTotal(userService.getCount());

        return response;
    }
}
