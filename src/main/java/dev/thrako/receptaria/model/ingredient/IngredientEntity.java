package dev.thrako.receptaria.model.ingredient;

import dev.thrako.receptaria.model.ingredient_name.IngredientNameEntity;
import dev.thrako.receptaria.model.unit.UnitEntity;
import dev.thrako.receptaria.model.recipe.RecipeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name="ingredients")
public class IngredientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ingredient_name_id")
    private IngredientNameEntity ingredientName;

    @Column
    private String quantity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "unit_id")
    private UnitEntity unit;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;

}
