package dev.thrako.receptaria.security;

import dev.thrako.receptaria.model.role.RoleEntity;
import dev.thrako.receptaria.model.user.UserEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collection;
import java.util.List;

import static dev.thrako.receptaria.constant.Constants.ROLE_PREFIX;

@Getter
@Setter
@Accessors(chain = true)
public class CurrentUser extends User {

    private Long id;

    private String firstName;

    private String lastName;

    private String displayName;

    public CurrentUser (String username, String password, Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);
    }

    public static CurrentUser fromEntity (UserEntity userEntity) {

        return new CurrentUser(userEntity.getEmail(),
                               userEntity.getPassword(),
                               userEntity.getRolesArray())
                .setId(userEntity.getId())
                .setDisplayName(userEntity.getDisplayName())
                .setFirstName(userEntity.getFirstName())
                .setLastName(userEntity.getLastName());
    }

}

