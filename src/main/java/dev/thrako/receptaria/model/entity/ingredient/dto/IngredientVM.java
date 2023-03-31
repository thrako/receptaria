package dev.thrako.receptaria.model.entity.ingredient.dto;

import dev.thrako.receptaria.model.entity.ingredient.IngredientEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class IngredientVM {

    private String ingredientName;

    private String quantity;

    private String unitName;

    public static IngredientVM fromEntity (IngredientEntity entity) {

        return new IngredientVM()
                .setIngredientName(entity.getIngredientName().getName())
                .setQuantity(entity.getQuantity())
                .setUnitName(entity.getUnit().getName());
    }

}
