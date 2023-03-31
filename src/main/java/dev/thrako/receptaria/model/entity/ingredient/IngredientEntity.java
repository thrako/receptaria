package dev.thrako.receptaria.model.entity.ingredient;

import dev.thrako.receptaria.model.entity.unit.UnitEntity;
import dev.thrako.receptaria.model.entity.ingredient_name.IngredientNameEntity;
import dev.thrako.receptaria.model.entity.recipe.RecipeEntity;
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ingredient_name_id")
    private IngredientNameEntity ingredientName;

    @Column
    private String quantity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "unit_id")
    private UnitEntity unit;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;

}
