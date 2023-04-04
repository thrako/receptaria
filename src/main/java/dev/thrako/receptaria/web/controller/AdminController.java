package dev.thrako.receptaria.web.controller;

import dev.thrako.receptaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController (UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/admin/dashboard")
    public String getDashboard () {

        return "admin/dashboard";
    }

    @GetMapping("/admin/users")
    public String getUsers (Model model) {

        model.addAttribute("users", this.userService.getAllUsersVM());

        return "admin/users";
    }

}
