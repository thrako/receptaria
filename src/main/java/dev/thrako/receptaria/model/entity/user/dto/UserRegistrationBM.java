package dev.thrako.receptaria.model.entity.user.dto;

import dev.thrako.receptaria.error.validation.annotation.PasswordsMatch;
import dev.thrako.receptaria.error.validation.annotation.UniqueEmail;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static dev.thrako.receptaria.constant.Constants.Registration.*;


@Getter
@Setter
@NoArgsConstructor
@PasswordsMatch()
public class UserRegistrationBM {

    @NotBlank(message = MSG_FIRST_NAME_BLANK_OR_EMPTY)
    private String firstName;

    @NotBlank(message = MSG_LAST_NAME_BLANK_OR_EMPTY)
    private String lastName;

    @NotBlank(message = MSG_DISPLAY_NAME_BLANK_OR_EMPTY)
    @Size(min = MIN_DISPLAY_NAME_LENGTH, message = MSG_MIN_DISPLAY_NAME)
    @Size(max = MAX_DISPLAY_NAME_LENGTH, message = MSG_MAX_DISPLAY_NAME)
    private String displayName;

    @Email(regexp = EMAIL_REG_EXP, message = MSG_NOT_WELL_FORMATTED_EMAIL)
    @UniqueEmail(message = MSG_EMAIL_NOT_AVAILABLE)
    private String email;

    @NotBlank(message = MSG_PASSWORD_EMPTY)
    @Size(min = MIN_PASSWORD_LENGTH, message = MSG_MIN_PASSWORD_LENGTH)
    private String password;

    @Size(min = MIN_PASSWORD_LENGTH, message = MSG_MIN_PASSWORD_LENGTH)
    private String confirmPassword;

}
