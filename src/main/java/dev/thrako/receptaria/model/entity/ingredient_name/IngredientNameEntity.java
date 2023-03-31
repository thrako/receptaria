package dev.thrako.receptaria.model.entity.ingredient_name;

import dev.thrako.receptaria.model.entity.ingredient.IngredientEntity;
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

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "ingredientName", cascade = CascadeType.ALL)
    private List<IngredientEntity> ingredients;

    public IngredientNameEntity (String name) {

        this.name = name;
    }
}
