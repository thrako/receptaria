package dev.thrako.receptaria.model.user.dto;

import dev.thrako.receptaria.model.recipe.dto.RecipeCardDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class UserProfileDTO {

    private String displayName;

    private boolean active;

    private List<RecipeCardDTO> userRecipes;

    public UserProfileDTO () {

        this.userRecipes = new ArrayList<>();
    }


}
