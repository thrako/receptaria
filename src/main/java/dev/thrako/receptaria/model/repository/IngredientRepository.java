package dev.thrako.receptaria.model.repository;

import dev.thrako.receptaria.model.entity.ingredient.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {

}
