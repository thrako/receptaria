package dev.thrako.receptaria.service;

import dev.thrako.receptaria.exception.RecipeNotFoundException;
import dev.thrako.receptaria.model.recipe.RecipeEntity;
import dev.thrako.receptaria.model.recipe.dto.RecipeCardDTO;
import dev.thrako.receptaria.model.user.UserEntity;
import dev.thrako.receptaria.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService (RecipeRepository recipeRepository){

        this.recipeRepository = recipeRepository;
    }

    public RecipeEntity save (RecipeEntity recipeEntity) {

        return recipeRepository.saveAndFlush(recipeEntity);
    }

    public boolean isAvailableRecipeTitle (Long principalId, String recipeTitle) {

        return !this.recipeRepository.existsByAuthor_IdAndTitle(principalId, recipeTitle);
    }

    public List<RecipeCardDTO> getRecipeCards () {

        return this.recipeRepository.findAll().stream()
                .map(RecipeCardDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<String> findAllRecipeTitlesByAuthor (UserEntity author) {
        return this.recipeRepository
                .findAllByAuthor(author)
                .stream()
                .map(RecipeEntity::getTitle)
                .toList();
    }

    public RecipeCardDTO getRecipeCard (Long id) {

        return this.recipeRepository
                .findById(id)
                .map(RecipeCardDTO::fromEntity)
                .orElseThrow(() -> new RecipeNotFoundException("No recipe with id %d found!".formatted(id)));
    }
}
