package dev.thrako.receptaria.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TermsController {

    @GetMapping("/terms")
    public String index () {

        return "terms";
    }

}
