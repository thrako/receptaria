package dev.thrako.receptaria.model.entity.user.dto;

import dev.thrako.receptaria.model.entity.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
public class UserProfileVM {

    private Long id;

    private String displayName;

    public static UserProfileVM fromEntity (UserEntity entity) {

        return new UserProfileVM()
                .setId(entity.getId())
                .setDisplayName(entity.getDisplayName());
    }


}
