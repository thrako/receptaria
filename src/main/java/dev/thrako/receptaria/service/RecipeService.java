package dev.thrako.receptaria.service;

import dev.thrako.receptaria.model.recipe.RecipeEntity;
import dev.thrako.receptaria.model.recipe.dto.RecipeDTO;
import dev.thrako.receptaria.model.user.UserEntity;
import dev.thrako.receptaria.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RecipeService {

    private final ModelMapper recipeMapper;
    private final UserService userService;
    private final RecipeRepository recipeRepository;
    private final PhotoRepository photoRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientNameRepository ingredientNameRepository;
    private final UnitRepository unitRepository;

    @Autowired
    public RecipeService (@Qualifier("recipeMapper") ModelMapper recipeMapper,
                          UserService userService,
                          RecipeRepository recipeRepository,
                          PhotoRepository photoRepository,
                          IngredientRepository ingredientRepository,
                          IngredientNameRepository ingredientNameRepository,
                          UnitRepository unitRepository) {

        this.recipeMapper = recipeMapper;
        this.userService = userService;
        this.recipeRepository = recipeRepository;
        this.photoRepository = photoRepository;
        this.ingredientRepository = ingredientRepository;
        this.ingredientNameRepository = ingredientNameRepository;
        this.unitRepository = unitRepository;
    }

    @Transactional
    public boolean save (RecipeDTO recipeDTO, Map<String, String[]> parameterMap) {


        RecipeEntity recipe = recipeMapper.map(recipeDTO, RecipeEntity.class);
        recipe.getPhotos().forEach(p -> p.setRecipe(recipe));
        recipe.getIngredients().forEach(i -> i.setRecipe(recipe));

        UserEntity author = userService.getCurrentUserEntity();
        recipe.setAuthor(author);

        recipeRepository.saveAndFlush(recipe);


        return this.recipeRepository
                .findRecipeByTitle(recipeDTO.getTitle())
                .isPresent();
    }


}
