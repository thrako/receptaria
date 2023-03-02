package dev.thrako.receptaria.service;

import dev.thrako.receptaria.model.recipe.RecipeEntity;
import dev.thrako.receptaria.model.recipe.dto.RecipeDTO;
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

    @Autowired
    public RecipeService (@Qualifier("recipeMapper") ModelMapper recipeMapper,
                          UserService userService,
                          RecipeRepository recipeRepository){

        this.recipeMapper = recipeMapper;
        this.userService = userService;
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    public boolean save (RecipeDTO recipeDTO, String email) {


        RecipeEntity recipe = recipeMapper.map(recipeDTO, RecipeEntity.class);
        recipe.getPhotos().forEach(p -> p.setRecipe(recipe));
        recipe.getIngredients().forEach(i -> i.setRecipe(recipe));

        UserEntity author = userService.getPrincipalEntity(email);
        recipe.setAuthor(author);

        recipeRepository.saveAndFlush(recipe);

        return this.recipeRepository
                .findRecipeByTitle(recipeDTO.getTitle())
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
