package dev.thrako.receptaria.controller.html;

import dev.thrako.receptaria.model.user.UserEntity;
import dev.thrako.receptaria.service.UserService;
import dev.thrako.receptaria.model.user.dto.UserLoginDTO;
import dev.thrako.receptaria.model.user.dto.UserRegistrationDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController (UserService userService) {

        this.userService = userService;
    }

    @ModelAttribute("userRegistrationDTO")
    public void initUserRegistrationDTO (Model model) {
        model.addAttribute("userRegistrationDTO", new UserRegistrationDTO());
    }

    @ModelAttribute("userLoginDTO")
    public void initUserLoginDTO (Model model) {
        model.addAttribute("userLoginDTO", new UserLoginDTO());
    }

    @GetMapping("/registration")
    public String register () {

        return "auth/registration";
    }

    @PostMapping("/registration")
    public String register (@Valid UserRegistrationDTO userRegistrationDTO,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttrs) {

        if (bindingResult.hasErrors()) {
            redirectAttrs.addFlashAttribute("userRegistrationDTO",
                                            userRegistrationDTO);
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationDTO",
                                            bindingResult);
            return "redirect:/registration";
        }

        if (this.userService.register(userRegistrationDTO)) {
            redirectAttrs.addFlashAttribute("userRegistrationDTO",
                    userRegistrationDTO);
            return "redirect:/registration/success";
        }

        return "error/500";
    }

    @GetMapping("/registration/success")
    public String getSuccessPage () {

        return "auth/registration-success";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/users/{id}")
    public String userProfile(Model model,
                              @PathVariable Long id) {

        Optional<UserEntity> userOpt = this.userService.findUserById(id);

        if (userOpt.isEmpty()) {
            return "error/404";
        }

        final UserEntity userEntity = userOpt.get();
        model.addAttribute("user", userEntity);

        return "profile/owner";
    }

    @GetMapping("/users/me")
    public String ownProfile (Principal principal) {

        final Long id = userService.getPrincipalEntity(principal.getName()).getId();
        return "redirect:/users/" + id;
    }

}
