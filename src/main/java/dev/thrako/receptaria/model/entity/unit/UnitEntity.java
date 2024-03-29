package dev.thrako.receptaria.model.entity.unit;

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
@Table(name="units")
public class UnitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "unit")
    private List<IngredientEntity> ingredients;

    public UnitEntity (String name) {

        this.name = name;
    }
}
