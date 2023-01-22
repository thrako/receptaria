package dev.thrako.receptaria.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserRegistrationDTO {

    @NotEmpty(message = "First name cannot be empty.")
    @Size(min = 2, max = 50, message = "Name size should be between 2 and 50.")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty.")
    @Size(min = 2, max = 50, message = "Name size should be between 2 and 50.")
    private String lastName;

    @NotEmpty(message = "Display name cannot be empty.")
    @Size(min = 2, max = 50, message = "Name size should be between 2 and 50.")
    private String displayName;

    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",
           message = "Please provide well formatted email")
    private String email;

    @NotEmpty(message = "Password cannot be empty.")
    @Min(value = 4, message = "Password length should be between 8 and 50 characters")
    private String password;

    @NotEmpty(message = "Field cannot be empty.")
    @Min(value = 4, message = "Password length should be between 8 and 50 characters")
    private String passwordRepeat;

}
