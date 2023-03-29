package dev.thrako.receptaria.security;

import dev.thrako.receptaria.model.role.RoleEntity;
import dev.thrako.receptaria.model.user.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

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
                               getRolesArray(userEntity.getRoles()))
                .setId(userEntity.getId())
                .setDisplayName(userEntity.getDisplayName())
                .setFirstName(userEntity.getFirstName())
                .setLastName(userEntity.getLastName());
    }

    private static List<GrantedAuthority> getRolesArray (List<RoleEntity> roles) {

        return AuthorityUtils.createAuthorityList(roles.stream()
                .map(entity -> ROLE_PREFIX.concat(entity.getRole().name()))
                .toArray(String[]::new));
    }

}

