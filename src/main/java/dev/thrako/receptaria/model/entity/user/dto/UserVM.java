package dev.thrako.receptaria.model.entity.user.dto;

import dev.thrako.receptaria.common.constant.Role;
import dev.thrako.receptaria.model.entity.role.RoleEntity;
import dev.thrako.receptaria.model.entity.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class UserVM {

    private Long id;

    private String firstName;

    private String lastName;

    private String displayName;

    private String email;

    private boolean active;

    private List<Role> roles;

    public static UserVM fromEntity (UserEntity entity) {

        return new UserVM()
                .setId(entity.getId())
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setDisplayName(entity.getDisplayName())
                .setEmail(entity.getEmail())
                .setActive(entity.isActive())
                .setRoles(entity.getRoles().stream().map(RoleEntity::getRole).toList());
    }

    public boolean isModerator () {
        return this.roles.contains(Role.MODERATOR);
    }

    public boolean isAdmin () {
        return this.roles.contains(Role.ADMIN);
    }



}
