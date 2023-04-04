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
public class UserShortVM {

    private Long id;

    private String displayName;

    public static UserShortVM fromEntity (UserEntity entity) {

        return new UserShortVM()
                .setId(entity.getId())
                .setDisplayName(entity.getDisplayName());
    }


}
