package dev.thrako.receptaria.model.user.dto;

import dev.thrako.receptaria.model.recipe.dto.RecipeShortDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class UserProfileDTO {

    private String displayName;

    private boolean active;

    private Set<RecipeShortDTO> userRecipes;

    public UserProfileDTO () {

        this.userRecipes = new HashSet<>();
    }


}
