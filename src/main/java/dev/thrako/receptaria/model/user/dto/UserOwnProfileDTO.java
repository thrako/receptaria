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
public class UserOwnProfileDTO {

    private String firstName;

    private String lastName;

    private String displayName;

    private String email;

    private boolean active;

    private boolean isPrincipal;

    private Set<RecipeShortDTO> likedRecipes;

    private Set<RecipeShortDTO> ownRecipes;

    public UserOwnProfileDTO () {

        this.likedRecipes = new HashSet<>();
        this.ownRecipes = new HashSet<>();
    }


}
