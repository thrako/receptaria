package dev.thrako.receptaria.common.security;

import dev.thrako.receptaria.common.constant.ContextAuthority;
import dev.thrako.receptaria.common.constant.ContextRole;
import dev.thrako.receptaria.common.constant.Role;
import dev.thrako.receptaria.model.entity.role.RoleEntity;
import dev.thrako.receptaria.model.entity.user.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.thrako.receptaria.common.constant.Constants.ROLE_PREFIX;

@Getter
@Setter
@Accessors(chain = true)
public class CurrentUser extends User {

    private Long id;

    private String firstName;

    private String lastName;

    private String displayName;

    private Map<ContextAuthority, Boolean> contextAuthorities;

    private Map<ContextRole, Boolean> contextRoles;

    public CurrentUser (String username, String password, Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);
        this.contextAuthorities = new HashMap<>();
        this.contextRoles = new HashMap<>();
        resetContextAuthorities();
        resetContextRoles();
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

    public boolean isAdmin() {

        return getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().contains(Role.ADMIN.name()));
    }

    public boolean isModerator() {

        return getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().contains(Role.MODERATOR.name()));
    }

    public void resetContextRoles () {

        this.contextRoles.put(ContextRole.AUTHOR, Boolean.FALSE);
        this.contextRoles.put(ContextRole.FOLLOWER, Boolean.FALSE);
        this.contextRoles.put(ContextRole.BLOCKED, Boolean.FALSE);
    }

    public void resetContextAuthorities () {

        this.contextAuthorities.put(ContextAuthority.VIEW, Boolean.FALSE);
        this.contextAuthorities.put(ContextAuthority.EDIT, Boolean.FALSE);
        this.contextAuthorities.put(ContextAuthority.DELETE, Boolean.FALSE);
    }

    public void setViewAuthority (Boolean flag) {

        this.contextAuthorities.put(ContextAuthority.VIEW, flag);
    }

    public void setEditAuthority (Boolean flag) {

        this.contextAuthorities.put(ContextAuthority.EDIT, flag);
    }

    public void setDeleteAuthority (Boolean flag) {

        this.contextAuthorities.put(ContextAuthority.DELETE, flag);
    }

    public Boolean has (ContextAuthority contextAuthority) {

        return this.contextAuthorities.get(contextAuthority);
    }

    private static List<GrantedAuthority> getRolesArray (List<RoleEntity> roles) {

        return AuthorityUtils.createAuthorityList(roles.stream()
                .map(entity -> ROLE_PREFIX.concat(entity.getRole().name()))
                .toArray(String[]::new));
    }

}

