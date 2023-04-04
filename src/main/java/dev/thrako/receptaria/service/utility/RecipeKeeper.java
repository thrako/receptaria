package dev.thrako.receptaria.service.utility;

import dev.thrako.receptaria.model.entity.ingredient.IngredientEntity;
import dev.thrako.receptaria.model.entity.ingredient.dto.IngredientBM;
import dev.thrako.receptaria.model.entity.photo.PhotoEntity;
import dev.thrako.receptaria.model.entity.photo.dto.PhotoDTO;
import dev.thrako.receptaria.model.entity.photo.dto.PhotoVM;
import dev.thrako.receptaria.model.entity.recipe.RecipeEntity;
import dev.thrako.receptaria.model.entity.recipe.dto.RecipeBM;
import dev.thrako.receptaria.model.entity.recipe.dto.RecipeCardVM;
import dev.thrako.receptaria.model.entity.recipe.dto.RecipeVM;
import dev.thrako.receptaria.model.entity.user.UserEntity;
import dev.thrako.receptaria.model.security.CurrentUser;
import dev.thrako.receptaria.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

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

        final UserEntity author = this.userService.findById(authorId);
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


    public boolean isAvailableRecipeTitleForAuthor (String title, CurrentUser author) {

        return recipeService.isAvailableRecipeTitle(title, author.getId());
    }

    public int addLike (Long recipeId, CurrentUser visitor) {

        final UserEntity visitorEntity = this.userService.findById(visitor.getId());
        final RecipeEntity recipeEntity = this.recipeService.findById(recipeId);

        if (!recipeEntity.addLike(visitorEntity) || !visitorEntity.likeRecipe(recipeEntity)) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        userService.saveAndFlush(visitorEntity);
        recipeService.saveAndFlush(recipeEntity);

        return recipeEntity.getLikes().size();
    }

    public int removeLike (Long recipeId, CurrentUser visitor) {

        final UserEntity visitorEntity = this.userService.findById(visitor.getId());
        final RecipeEntity recipeEntity = this.recipeService.findById(recipeId);

        if (!recipeEntity.removeLike(visitorEntity) || !visitorEntity.unlikeRecipe(recipeEntity)) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        userService.saveAndFlush(visitorEntity);
        recipeService.saveAndFlush(recipeEntity);

        return recipeEntity.getLikes().size();
    }

    public RecipeVM getRecipeVMForUser (Long id, CurrentUser currentUser) {

        final UserEntity userEntity = this.userService.findById(currentUser.getId());
        final RecipeEntity recipeEntity = this.recipeService.findById(id);

        return RecipeVM.fromEntity(recipeEntity)
                .setLiked(recipeEntity.getLikes().contains(userEntity));
    }

    public void process (RecipeBM recipeBM) {

        final UUID tempRecipeId = recipeBM.getTempRecipeId();
        final Long primaryPhotoId = recipeBM.getPrimaryPhotoId();

        this.tempPhotoService.updatePrimaryFlag(tempRecipeId, primaryPhotoId);

        final List<PhotoVM> photoVMList = this.tempPhotoService.getListPhotoVM(tempRecipeId);
        recipeBM.setPhotoVMList(photoVMList);
    }

    public List<String> getDistinctUnitNames () {

        return this.unitService.getDistinctUnitNames();
    }

    public List<RecipeCardVM> getCardsAll (CurrentUser currentUser) {

        return this.recipeService.getCardsAll(currentUser);
    }

    public List<RecipeCardVM> getCardsOwn (CurrentUser currentUser) {

        return this.recipeService.getCardsOwn(currentUser);
    }

    public List<RecipeCardVM> getCardsByAuthor (CurrentUser currentUser, Long authorId) {

        return this.recipeService.getRecipeCardsByAuthor(currentUser, authorId);
    }
}

