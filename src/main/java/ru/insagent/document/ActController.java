package ru.insagent.document;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ActController {
    @GetMapping("/act.html")
    public String main() {
        return "journal/act";
    }
}
