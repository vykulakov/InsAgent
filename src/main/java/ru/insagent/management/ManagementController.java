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

package ru.insagent.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.insagent.management.model.CityFilter;
import ru.insagent.management.model.UnitFilter;
import ru.insagent.management.model.UserFilter;
import ru.insagent.model.Response;
import ru.insagent.service.CityService;
import ru.insagent.service.UnitService;
import ru.insagent.service.UserService;

@Controller
public class ManagementController {
    private CityService cityService;
    private UnitService unitService;
    private UserService userService;

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @Autowired
    public void setUnitService(UnitService unitService) {
        this.unitService = unitService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/management/city")
    public String city() {
        return "management/city";
    }

    @ResponseBody
    @RequestMapping("/management/cities")
    public Response cities(
            CityFilter filter,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset
    ) {
        Response response = new Response();
        response.setRows(cityService.listByUser(null, filter, sort, order, limit, offset));
        response.setTotal(cityService.getCount());

        return response;
    }

    @RequestMapping("/management/unit")
    public String unit(Model model) {
        model.addAttribute("types", unitService.types());
        model.addAttribute("cities", cityService.list());

        return "management/unit";
    }

    @ResponseBody
    @RequestMapping("/management/units")
    public Response units(
            UnitFilter filter,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset
    ) {
        Response response = new Response();
        response.setRows(unitService.listByUser(null, filter, sort, order, limit, offset));
        response.setTotal(unitService.getCount());

        return response;
    }

    @RequestMapping("/management/user")
    public String user(Model model) {
        model.addAttribute("roles", userService.roles());
        model.addAttribute("units", userService.units());

        return "management/user";
    }

    @ResponseBody
    @RequestMapping("/management/users")
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
