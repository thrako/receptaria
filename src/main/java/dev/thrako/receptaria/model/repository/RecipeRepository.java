package dev.thrako.receptaria.model.repository;

import dev.thrako.receptaria.model.entity.recipe.RecipeEntity;
import dev.thrako.receptaria.model.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    boolean existsByAuthor_IdAndTitle (Long id, String title);

    List<RecipeEntity> findByAuthor_Id (Long id);

    List<RecipeEntity> findAllByAuthor (UserEntity author);
}
