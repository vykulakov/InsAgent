package ru.insagent.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.insagent.document.model.ActFilter;
import ru.insagent.document.model.BsoFilter;
import ru.insagent.model.Response;
import ru.insagent.model.Roles;
import ru.insagent.service.ActService;

@Controller
public class ActController {
    private ActService actService;

    @Autowired
    public void setActService(ActService actService) {
        this.actService = actService;
    }

    @GetMapping("/act.html")
    public String main() {
        return "journal/act";
    }

    @ResponseBody
    @GetMapping("/act")
    public Response list(
            ActFilter filter,
            Authentication authentication,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset
    ) {
        Roles roles = new Roles(authentication);

        Response response = new Response();
        response.setRows(actService.list(roles, filter, sort, order, limit, offset));
        response.setTotal(actService.getCount());

        return response;
    }
}
