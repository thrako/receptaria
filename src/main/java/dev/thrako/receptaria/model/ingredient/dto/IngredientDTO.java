package dev.thrako.receptaria.model.ingredient.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class IngredientDTO {

    @NotBlank(message = "Ingredient name should not be blank")
    private String ingredientName;

    private String quantity;

    private String unitName;

    public IngredientDTO (String ingredientName, String quantity, String unitName) {

        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unitName = unitName;
    }
}
