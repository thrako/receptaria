package dev.thrako.receptaria.repository;

import dev.thrako.receptaria.model.recipe.RecipeEntity;
import dev.thrako.receptaria.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    Optional<RecipeEntity> findRecipeByTitleAndAuthor (String name, UserEntity author);

    Optional<RecipeEntity> findRecipeByTitle (String title);
}
