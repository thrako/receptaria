package dev.thrako.receptaria.web.controllers;

import dev.thrako.receptaria.security.CurrentUser;
import dev.thrako.receptaria.user.UserEntity;
import dev.thrako.receptaria.user.UserService;
import dev.thrako.receptaria.user.dto.UserLoginDTO;
import dev.thrako.receptaria.user.dto.UserRegistrationDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final CurrentUser currentUser;

    @Autowired
    public UserController (UserService userService,
                           CurrentUser currentUser) {

        this.userService = userService;
        this.currentUser = currentUser;
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

        return "registration";
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

        return "error500";
    }

    @GetMapping("/registration/success")
    public String getSuccessPage () {

        return "registrationSuccess";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid UserLoginDTO userLoginDTO,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttrs) {

        if (bindingResult.hasErrors() || !this.userService.login(userLoginDTO)) {
            ObjectError error = new ObjectError("globalError", "Incorrect username or password!");
            bindingResult.addError(error);
            redirectAttrs
                    .addFlashAttribute("userLoginDTO", userLoginDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userLoginDTO", bindingResult);

            return "redirect:login";
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        if (!userService.isLoggedIn()){
            return "redirect:/users/login";
        }
        this.userService.logout();
        return "redirect:/";
    }

    @GetMapping("/users/{id}")
    public String userProfile(Model model,
                              @PathVariable Long id) {

        Optional<UserEntity> userOpt = this.userService.findById(id);

        if (userOpt.isEmpty()) {
            return "error404";
        }

        final UserEntity userEntity = userOpt.get();
        model.addAttribute("user", userEntity);

        if (id.equals(this.currentUser.getId())) {
            return "myprofile";
        } else {
            return "profile";
        }
    }



}
