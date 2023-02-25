package dev.thrako.receptaria.model.ingredient.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class IngredientDTO {

    private String ingredientName;
    private String quantity;
    private String unitName;

    public IngredientDTO (String ingredientName, String quantity, String unitName) {

        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unitName = unitName;
    }
}
