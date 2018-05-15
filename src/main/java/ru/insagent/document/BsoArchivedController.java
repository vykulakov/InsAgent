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

package ru.insagent.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.insagent.document.model.BsoFilter;
import ru.insagent.model.Response;
import ru.insagent.model.Roles;
import ru.insagent.service.ArchivedService;
import ru.insagent.service.BsoService;

@Controller
public class BsoArchivedController {
    private ArchivedService archivedService;

    @Autowired
    public void setArchivedService(ArchivedService archivedService) {
        this.archivedService = archivedService;
    }

    @GetMapping("/archived.html")
    public String main(Model model, Authentication authentication) {
        Roles roles = new Roles(authentication);

        model.addAttribute("units", archivedService.units(roles));
        model.addAttribute("types", archivedService.types(roles));
        model.addAttribute("nodes", archivedService.nodes(roles));
        model.addAttribute("actions", archivedService.actions(roles));

        return "journal/archived";
    }

    @ResponseBody
    @GetMapping("/archived")
    public Response list(
            BsoFilter filter,
            Authentication authentication,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset
    ) {
        Roles roles = new Roles(authentication);

        Response response = new Response();
        response.setRows(archivedService.listByRoles(roles, filter, sort, order, limit, offset));
        response.setTotal(archivedService.getCount());

        return response;
    }
}
