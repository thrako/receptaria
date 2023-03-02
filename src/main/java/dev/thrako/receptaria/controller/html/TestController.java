package dev.thrako.receptaria.controller.html;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    public TestController () {

    }

    @GetMapping("/test")
    public String test () {

        return "test";
    }

}
