package dev.thrako.receptaria.model.entity.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginBM {

    private String email;

    private String password;

}
