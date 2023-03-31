package dev.thrako.receptaria.service.utility;

import dev.thrako.receptaria.model.entity.ingredient.IngredientEntity;
import dev.thrako.receptaria.model.entity.ingredient.dto.IngredientBM;
import dev.thrako.receptaria.model.entity.photo.PhotoEntity;
import dev.thrako.receptaria.model.entity.photo.dto.PhotoDTO;
import dev.thrako.receptaria.model.entity.recipe.RecipeEntity;
import dev.thrako.receptaria.model.entity.recipe.dto.RecipeBM;
import dev.thrako.receptaria.model.entity.user.UserEntity;
import dev.thrako.receptaria.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecipeKeeper {

    private final RecipeService recipeService;
    private final TempPhotoService tempPhotoService;
    private final PhotoService photoService;
    private final IngredientService ingredientService;
    private final IngredientNameService ingredientNameService;
    private final UnitService unitService;
    private final UserService userService;
    private final CloudUtility cloudUtility;

    public RecipeKeeper (RecipeService recipeService,
                         TempPhotoService tempPhotoService,
                         PhotoService photoService,
                         IngredientService ingredientService,
                         IngredientNameService ingredientNameService,
                         UnitService unitService,
                         UserService userService,
                         CloudUtility cloudUtility) {

        this.recipeService = recipeService;
        this.tempPhotoService = tempPhotoService;
        this.photoService = photoService;
        this.ingredientService = ingredientService;
        this.ingredientNameService = ingredientNameService;
        this.unitService = unitService;
        this.userService = userService;
        this.cloudUtility = cloudUtility;
    }

    public Long save (RecipeBM recipeBM, Long authorId) {

        RecipeEntity newRecipeEntity = recipeBM.toEntity();

        final UserEntity author = this.userService.getUserEntity(authorId);
        newRecipeEntity.setAuthor(author);

        final RecipeEntity savedRecipeEntity = this.recipeService.save(newRecipeEntity);

        final List<IngredientEntity> ingredientEntities = recipeBM.getListIngredientBM()
                .stream()
                .map(this::IngredientEntityFromDto)
                .map(ingredientEntity -> ingredientEntity.setRecipe(savedRecipeEntity))
                .toList();

        final List<PhotoDTO> photoDTOList = this.tempPhotoService.getListPhotoDTO(recipeBM.getTempRecipeId());

        final List<PhotoEntity> photoEntities = this.cloudUtility
                .moveAll(photoDTOList, savedRecipeEntity.getId())
                .stream()
                .map(PhotoDTO::toEntity)
                .map(photoEntity -> photoEntity.setRecipe(savedRecipeEntity))
                .toList();

        final RecipeEntity completeRecipeEntity = savedRecipeEntity
                .setIngredients(this.ingredientService.saveAllAndFlush(ingredientEntities))
                .setPhotos(this.photoService.saveAllAndFlush(photoEntities));

        return this.recipeService.save(completeRecipeEntity).getId();
    }

    private IngredientEntity IngredientEntityFromDto (@Valid IngredientBM dto) {

        return new IngredientEntity()
                .setIngredientName(ingredientNameService.getOrCreateByName(dto.getIngredientName()))
                .setQuantity(dto.getQuantity())
                .setUnit(unitService.getOrCreateByName(dto.getUnitName()));

    }


}

