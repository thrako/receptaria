package dev.thrako.receptaria.controller.rest;

import dev.thrako.receptaria.model.user.dto.UserProfileDTO;
import dev.thrako.receptaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestUserController {

    private final UserService userService;

    @Autowired
    public RestUserController (UserService userService) {

        this.userService = userService;
    }

    @GetMapping("api/users/{id}")
    public UserProfileDTO userProfile(@PathVariable Long id) {

        return this.userService.getUserProfileById(id);

    }

}
