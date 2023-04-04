package dev.thrako.receptaria.web.controller;

import dev.thrako.receptaria.model.security.CurrentUser;
import dev.thrako.receptaria.model.entity.user.dto.UserVM;
import dev.thrako.receptaria.service.UserService;
import dev.thrako.receptaria.model.entity.user.dto.UserRegistrationBM;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

import static dev.thrako.receptaria.common.constant.Constants.BINDING_RESULT_PATH;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController (UserService userService) {

        this.userService = userService;
    }

    @ModelAttribute("userRegistrationBM")
    public void initUserRegistrationBM (Model model) {

        model.addAttribute("userRegistrationBM", new UserRegistrationBM());
    }

    @GetMapping("/registration")
    public String getRegistrationForm () {


        return "auth/registration";
    }

    @PostMapping("/registration")
    public String doRegister (@Valid UserRegistrationBM userRegistrationBM,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttrs) {

        checkPasswordsMatchError(bindingResult);

        if (bindingResult.hasErrors()) {
            redirectAttrs.addFlashAttribute("userRegistrationBM", userRegistrationBM);
            redirectAttrs.addFlashAttribute(BINDING_RESULT_PATH + userRegistrationBM, bindingResult);
            return "redirect:/registration";
        }

        this.userService.register(userRegistrationBM);
        redirectAttrs.addFlashAttribute("displayName", userRegistrationBM.getDisplayName());

        return "redirect:/registration/success";
    }

    @GetMapping("/registration/success")
    public String getSuccessPage () {

        return "auth/registration-success";
    }

    @GetMapping("/login")
    public String getLoginForm () {

        return "auth/login";
    }

    @GetMapping(value = "/users/me", consumes = MediaType.ALL_VALUE)
    public String getOwnProfile (Model model,
                                 @AuthenticationPrincipal CurrentUser currentUser) {

        model.addAttribute("displayName", currentUser.getDisplayName());

        return "profile/owner";
    }

    @GetMapping("/users/{id:^[0-9]*}")
    public String getUserProfile (Model model,
                                  @PathVariable Long id) {


        final UserVM userVM = this.userService.getUserVMById(id);
        model.addAttribute("displayName", userVM.getDisplayName());
        model.addAttribute("userId", userVM.getId());

        return "profile/other";
    }

    private static void checkPasswordsMatchError (BindingResult bindingResult) {

        final Predicate<ObjectError> hasPasswordsMatchErrorCode =
                err -> Arrays.asList(Objects.requireNonNull(err.getCodes())).contains("PasswordsMatch");

        bindingResult
                .getGlobalErrors()
                .stream()
                .filter(hasPasswordsMatchErrorCode)
                .findAny()
                .ifPresent(objectError -> bindingResult
                        .rejectValue("confirmPassword",
                                     Objects.requireNonNull(objectError.getCode()),
                                     Objects.requireNonNull(objectError.getDefaultMessage())));
    }

}
