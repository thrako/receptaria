package dev.thrako.receptaria.model.recipe.dto;

import dev.thrako.receptaria.model.ingredient.dto.IngredientDTO;
import dev.thrako.receptaria.model.photo.dto.PhotoDTO;
import dev.thrako.receptaria.model.user.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class RecipeDTO {

    @NotEmpty(message = "Title is required.")
    private String title;

    private List<PhotoDTO> photoDTOS;

    private List<IngredientDTO> ingredientDTOS;

    private int preparationHours;

    private int preparationMinutes;

    private int cookingHours;

    private int cookingMinutes;

    private int servings;

    @NotEmpty(message = "Description is required.")
    private String description;

    private UserEntity author;

    public RecipeDTO () {

        this.photoDTOS = new ArrayList<>();
        this.ingredientDTOS = new ArrayList<>();
    }

    public RecipeDTO addPhotoDTO (PhotoDTO photoDTO) {

        this.photoDTOS.add(photoDTO);

        return this;
    }

    public RecipeDTO removePhotoDTO (PhotoDTO photoDTO) {

        this.photoDTOS.remove(photoDTO);

        return this;
    }
}
