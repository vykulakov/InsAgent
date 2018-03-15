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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.insagent.management.city.model.CityFilter;
import ru.insagent.management.unit.model.UnitFilter;
import ru.insagent.management.user.model.UserFilter;
import ru.insagent.model.City;
import ru.insagent.model.Response;
import ru.insagent.service.CityService;
import ru.insagent.service.UnitService;
import ru.insagent.service.UserService;

import javax.websocket.server.PathParam;

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

    @GetMapping("/management/city")
    public String city() {
        return "management/city";
    }

    @ResponseBody
    @GetMapping("/management/cities")
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

    @ResponseBody
    @GetMapping("/management/city/{id}")
    public City getCity(
            @PathVariable(name = "id") int id
    ) {
        return cityService.getEditable(id);
    }

    @ResponseStatus
    @PostMapping("/management/city")
    public ResponseEntity updateCity(
            City city
    ) {
        cityService.update(city);

        return new ResponseEntity(HttpStatus.OK);
    }

    @ResponseStatus
    @PostMapping("/management/city/{id}")
    public ResponseEntity removeCity(
            @PathVariable(name = "id") int id
    ) {
        cityService.remove(id);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/management/unit")
    public String unit(Model model) {
        model.addAttribute("types", unitService.types());
        model.addAttribute("cities", cityService.list());

        return "management/unit";
    }

    @ResponseBody
    @GetMapping("/management/units")
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
