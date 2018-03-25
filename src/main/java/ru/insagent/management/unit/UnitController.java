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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.insagent.management.unit.model.UnitFilter;
import ru.insagent.model.Response;
import ru.insagent.model.Unit;
import ru.insagent.service.CityService;
import ru.insagent.service.UnitService;

@Controller
public class UnitController {
    private CityService cityService;
    private UnitService unitService;

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @Autowired
    public void setUnitService(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping("/management/unit.html")
    public String unit(Model model) {
        model.addAttribute("types", unitService.types());
        model.addAttribute("cities", cityService.list());

        return "management/unit";
    }

    @ResponseBody
    @GetMapping("/management/unit/{id}")
    public Unit get(
            @PathVariable(name = "id") int id
    ) {
        return unitService.getEditable(id);
    }

    @ResponseBody
    @GetMapping("/management/unit")
    public Response list(
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

    @ResponseStatus
    @PostMapping("/management/unit")
    public ResponseEntity update(
            Unit unit
    ) {
        unitService.update(unit);

        return new ResponseEntity(HttpStatus.OK);
    }

    @ResponseStatus
    @DeleteMapping("/management/unit/{id}")
    public ResponseEntity remove(
            @PathVariable(name = "id") int id
    ) {
        unitService.remove(id);

        return new ResponseEntity(HttpStatus.OK);
    }
}
