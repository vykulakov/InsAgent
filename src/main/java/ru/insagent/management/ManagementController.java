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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.insagent.management.model.CityFilter;
import ru.insagent.model.City;
import ru.insagent.model.Response;
import ru.insagent.model.User;
import ru.insagent.service.CityService;

import java.security.Principal;
import java.util.List;

@Controller
public class ManagementController {
    private CityService cityService;

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @RequestMapping("/management/city")
    public String city() {
        return "management/city";
    }

    @RequestMapping("/management/cities")
    public ResponseEntity<Response> cities(
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "filter", required = false) CityFilter filter
    ) {
        Response response = new Response();
        if (filter == null) {
            response.setRows(cityService.listByUser(null, search, sort, order, limit, offset));
        } else {
            response.setRows(cityService.listByUser(null, filter, sort, order, limit, offset));
        }
        response.setTotal(cityService.getCount());

        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @RequestMapping("/management/unit")
    public String unit() {
        return "management/unit";
    }

    @RequestMapping("/management/user")
    public String user() {
        return "management/user";
    }
}
