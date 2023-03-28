package dev.thrako.receptaria.model.recipe.dto;

import dev.thrako.receptaria.constant.VisibilityStatus;
import dev.thrako.receptaria.model.ingredient.dto.IngredientDTO;
import dev.thrako.receptaria.model.photo.dto.SavedPhotoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class RecipeBM {

    private UUID tempRecipeId;

    @NotEmpty(message = "Title is required.")
    private String title;

    private List<SavedPhotoDTO> savedPhotoDTOS;

    private Long primaryPhotoId;

    private List<@Valid IngredientDTO> ingredientDTOS;

    @PositiveOrZero(message = "Preparation hours should not be negative.")
    private int preparationHours;

    @PositiveOrZero(message = "Preparation minutes should not be negative.")
    private int preparationMinutes;

    @PositiveOrZero(message = "Cooking hours should not be negative.")
    private int cookingHours;

    @PositiveOrZero(message = "Cooking minutes should not be negative.")
    private int cookingMinutes;

    @Positive(message = "Servings should be positive number.")
    private int servings;

    @NotEmpty(message = "Description is required.")
    private String description;

    @NotNull(message = "Visibility status is required.")
    private VisibilityStatus visibilityStatus;

    private Long authorId;

    public RecipeBM () {

        this.savedPhotoDTOS = new ArrayList<>();
        this.ingredientDTOS = new ArrayList<>();
    }

}
