package dev.thrako.receptaria.service;

import dev.thrako.receptaria.model.photo.dto.SavedPhotoDTO;
import dev.thrako.receptaria.model.recipe.RecipeEntity;
import dev.thrako.receptaria.model.recipe.dto.RecipeBM;
import dev.thrako.receptaria.model.recipe.dto.RecipeShortDTO;
import dev.thrako.receptaria.model.user.UserEntity;
import dev.thrako.receptaria.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final ModelMapper recipeMapper;
    private final UserService userService;
    private final RecipeRepository recipeRepository;
    private final TempPhotoRepository tempPhotoRepository;

    @Autowired
    public RecipeService (@Qualifier("recipeMapper") ModelMapper recipeMapper,
                          UserService userService,
                          RecipeRepository recipeRepository,
                          TempPhotoRepository tempPhotoRepository){

        this.recipeMapper = recipeMapper;
        this.userService = userService;
        this.recipeRepository = recipeRepository;
        this.tempPhotoRepository = tempPhotoRepository;
    }

    @Transactional
    public boolean save (RecipeBM recipeBM, String email) {


        RecipeEntity recipe = recipeMapper.map(recipeBM, RecipeEntity.class);
        recipe.getIngredients().forEach(i -> i.setRecipe(recipe));

        UserEntity author = userService.getPrincipalEntity(email);
        recipe.setAuthor(author);

        recipeRepository.saveAndFlush(recipe);

        return this.recipeRepository
                .findRecipeByTitle(recipeBM.getTitle())
                .isPresent();
    }


    public boolean isAvailableRecipeTitle (Long principalId, String recipeTitle) {

        return !this.recipeRepository.existsByAuthor_IdAndTitle(principalId, recipeTitle);
    }



    public List<RecipeShortDTO> getRecipeCards () {

        return this.recipeRepository.findAll().stream()
                .map(RecipeShortDTO::fromEntity)
                .collect(Collectors.toList());

    }


}
