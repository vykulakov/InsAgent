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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.insagent.management.city.model.CityFilter;
import ru.insagent.model.City;
import ru.insagent.model.Response;
import ru.insagent.service.CityService;

@Controller
public class CityController {
    private CityService cityService;

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/management/city.html")
    public String main() {
        return "management/city";
    }

    @ResponseBody
    @GetMapping("/management/city/{id}")
    public City get(
            @PathVariable(name = "id") int id
    ) {
        return cityService.getEditable(id);
    }

    @ResponseBody
    @GetMapping("/management/city")
    public Response list(
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

    @ResponseStatus
    @PostMapping("/management/city")
    public ResponseEntity update(
            City city
    ) {
        cityService.update(city);

        return new ResponseEntity(HttpStatus.OK);
    }

    @ResponseStatus
    @DeleteMapping("/management/city/{id}")
    public ResponseEntity remove(
            @PathVariable(name = "id") int id
    ) {
        cityService.remove(id);

        return new ResponseEntity(HttpStatus.OK);
    }
}
