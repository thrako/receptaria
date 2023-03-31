package dev.thrako.receptaria.service;

import dev.thrako.receptaria.constant.VisibilityStatus;
import dev.thrako.receptaria.error.exception.RecipeNotFoundException;
import dev.thrako.receptaria.model.entity.recipe.RecipeEntity;
import dev.thrako.receptaria.model.entity.recipe.dto.RecipeCardVM;
import dev.thrako.receptaria.model.entity.recipe.dto.RecipeVM;
import dev.thrako.receptaria.model.entity.user.UserEntity;
import dev.thrako.receptaria.model.repository.RecipeRepository;
import dev.thrako.receptaria.security.CurrentUser;
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

    public List<RecipeCardVM> getRecipeCards () {

        return this.recipeRepository.findAll().stream()
                .map(RecipeCardVM::fromEntity)
                .collect(Collectors.toList());
    }

    public List<String> findAllRecipeTitlesByAuthor (UserEntity author) {
        return this.recipeRepository
                .findAllByAuthor(author)
                .stream()
                .map(RecipeEntity::getTitle)
                .toList();
    }

    public RecipeVM getRecipeVM (Long id) {

        return this.recipeRepository
                .findById(id)
                .map(RecipeVM::fromEntity)
                .orElseThrow(() -> new RecipeNotFoundException("No recipe with id %d found!".formatted(id)));
    }
    public boolean checkCanView (CurrentUser currentUser, Long recipeId) {

        final RecipeEntity recipeEntity = findById(recipeId);

        if (isAuthor(currentUser, recipeEntity) || currentUser.isAdmin()) {

            return Boolean.TRUE;
        }

        final VisibilityStatus visibilityStatus = recipeEntity.getVisibilityStatus();

        switch (visibilityStatus) {
            case PRIVATE -> {
                return Boolean.FALSE;
            }
            case FOLLOWERS -> {
                //TODO if (is in followers list) return TRUE;
            }
            case PUBLIC -> {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    public Boolean checkCanEdit (CurrentUser currentUser, long recipeId) {

        final RecipeEntity recipeEntity = findById(recipeId);

        if (isAuthor(currentUser, recipeEntity) || currentUser.isAdmin()) {

            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public Boolean checkCanDelete (CurrentUser currentUser, long recipeId) {

        final RecipeEntity recipeEntity = findById(recipeId);

        if (isAuthor(currentUser, recipeEntity) || currentUser.isAdmin()) {

            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public RecipeEntity findById (long recipeId) {

        return this.recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("No recipe with id %d found!".formatted(recipeId)));
    }

    public boolean isAuthor (CurrentUser currentUser, long recipeId) {

        return isAuthor(currentUser, findById(recipeId));
    }

    private static boolean isAuthor (CurrentUser currentUser, RecipeEntity recipeEntity) {

        return currentUser.getId().equals(recipeEntity.getAuthor().getId());
    }
}
