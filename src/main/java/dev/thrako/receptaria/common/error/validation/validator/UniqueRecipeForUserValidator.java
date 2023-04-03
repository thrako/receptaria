package dev.thrako.receptaria.common.error.validation.validator;

import dev.thrako.receptaria.model.entity.recipe.dto.RecipeBM;
import dev.thrako.receptaria.model.entity.user.UserEntity;
import dev.thrako.receptaria.service.RecipeService;
import dev.thrako.receptaria.service.UserService;
import dev.thrako.receptaria.common.error.validation.annotation.UniqueRecipeForUser;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
public class UniqueRecipeForUserValidator implements ConstraintValidator<UniqueRecipeForUser, RecipeBM> {

    private final RecipeService recipeService;
    private final UserService userService;

    public UniqueRecipeForUserValidator (RecipeService recipeService,
                                         UserService userService) {

        this.recipeService = recipeService;
        this.userService = userService;
    }

    @Override
    public void initialize (UniqueRecipeForUser constraintAnnotation) {

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid (RecipeBM candidate, ConstraintValidatorContext context) {

        final String recipeTitle = candidate.getTitle();

        final Long authorId = candidate.getAuthorId();
        final UserEntity userEntity = this.userService.findById(authorId);

        return !this.recipeService.findAllRecipeTitlesByAuthor(userEntity).contains(recipeTitle);
    }

}
