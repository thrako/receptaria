package dev.thrako.receptaria.controller;

import dev.thrako.receptaria.constant.Constants;
import dev.thrako.receptaria.model.user.UserEntity;
import dev.thrako.receptaria.security.CurrentUser;
import dev.thrako.receptaria.service.UserService;
import dev.thrako.receptaria.model.user.dto.UserRegistrationBM;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
    public String ownProfile (Model model, @AuthenticationPrincipal CurrentUser currentUser) {

        model.addAttribute("displayName", currentUser.getDisplayName());

        return "profile/owner";
    }

    @GetMapping("/users/{id:^[0-9]*}")
    public String userProfile (Model model,
                               @PathVariable Long id) {

        final String displayName = this.userService
                .findUserById(id)
                .map(UserEntity::getDisplayName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user exists!"));

        model.addAttribute("displayName", displayName);

        return "profile/other";
    }

}
