package dev.thrako.receptaria.controller;

import dev.thrako.receptaria.constant.Constants;
import dev.thrako.receptaria.model.user.UserEntity;
import dev.thrako.receptaria.service.UserService;
import dev.thrako.receptaria.model.user.dto.UserRegistrationBM;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController (UserService userService) {

        this.userService = userService;
    }

    @ModelAttribute("userRegistrationBM")
    public void initUserLoginBM (Model model) {

        model.addAttribute("userRegistrationBM", new UserRegistrationBM());
    }

    @GetMapping("/registration")
    public String register (Model model) {

        model.addAttribute("validationConstraints", Constants.Registration.constraints);
        model.addAttribute("validationMessages", Constants.Registration.messages);

        return "auth/registration";
    }

    @PostMapping("/registration")
    public String register (@Valid UserRegistrationBM userRegistrationBM,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttrs) {

        checkPasswordsMatchError(bindingResult);

        if (bindingResult.hasErrors()) {
            redirectAttrs.addFlashAttribute("userRegistrationBM",
                    userRegistrationBM);
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationBM",
                    bindingResult);
            return "redirect:/registration";
        }

        if (this.userService.register(userRegistrationBM)) {
            redirectAttrs.addFlashAttribute("userRegistrationBM",
                    userRegistrationBM);
            return "redirect:/registration/success";
        }

        return "error/500";
    }

    private static void checkPasswordsMatchError (BindingResult bindingResult) {

        final Predicate<ObjectError> hasPasswordsMatchErrorCode =
                err -> Arrays.asList(Objects.requireNonNull(err.getCodes())).contains("PasswordsMatch");

        bindingResult
                .getGlobalErrors()
                .stream()
                .filter(hasPasswordsMatchErrorCode)
                .findAny()
                .ifPresent(objectError -> {
                    bindingResult.rejectValue("confirmPassword",
                            Objects.requireNonNull(objectError.getCode()),
                            Objects.requireNonNull(objectError.getDefaultMessage()));
                });
    }

    @GetMapping("/registration/success")
    public String getSuccessPage () {

        return "auth/registration-success";
    }

    @GetMapping("/login")
    public String login () {

        return "auth/login";
    }

    @GetMapping(value = "/users/me", consumes = MediaType.ALL_VALUE)
    public String ownProfile (Model model, Principal principal) {

        final UserEntity userEntity = userService.getPrincipalEntity(principal.getName());

        model.addAttribute("user", userEntity);

        return "profile/owner";
    }

    @GetMapping("/users/{id:^[0-9]*}")
    public String userProfile (Model model,
                               @PathVariable Long id) {

        Optional<UserEntity> userOpt = this.userService.findUserById(id);

        if (userOpt.isEmpty()) {
            return "error/404";
        }

        final UserEntity userEntity = userOpt.get();
        model.addAttribute("user", userEntity);

        return "profile/other";
    }

}
