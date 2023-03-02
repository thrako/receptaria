package dev.thrako.receptaria.repository;

import dev.thrako.receptaria.model.recipe.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    boolean existsByAuthor_IdAndTitle (Long id, String title);

    Optional<RecipeEntity> findRecipeByTitle (String title);

    Set<RecipeEntity> findByAuthor_Id (Long id);
}
