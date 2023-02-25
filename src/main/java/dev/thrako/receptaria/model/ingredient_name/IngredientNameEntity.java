package dev.thrako.receptaria.model.ingredient_name;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "ingredient_names")
public class IngredientNameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "ingredientName", cascade = CascadeType.ALL)
    private List<dev.thrako.receptaria.model.ingredient.IngredientEntity> ingredients;

    public IngredientNameEntity (String name) {

        this.name = name;
    }
}
