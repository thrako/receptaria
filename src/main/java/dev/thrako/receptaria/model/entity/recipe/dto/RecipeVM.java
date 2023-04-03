package dev.thrako.receptaria.model.entity.recipe.dto;

import dev.thrako.receptaria.common.constant.VisibilityStatus;
import dev.thrako.receptaria.model.entity.ingredient.dto.IngredientVM;
import dev.thrako.receptaria.model.entity.photo.dto.PhotoVM;
import dev.thrako.receptaria.model.entity.recipe.RecipeEntity;
import dev.thrako.receptaria.model.entity.user.dto.UserProfileVM;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class RecipeVM {

    private Long id;

    private String title;

    private List<PhotoVM> listPhotoVM;

    private List<IngredientVM> listIngredientVM;

    private String preparationTime;

    private String cookingTime;

    private Integer servings;

    private List<UserProfileVM> likes;

    private boolean isLiked;

    private String description;

    private UserProfileVM author;

    private VisibilityStatus visibilityStatus;

    private LocalDateTime addedOn;

    private LocalDateTime lastUpdated;

    public static RecipeVM fromEntity (RecipeEntity entity) {
        return new RecipeVM()
                .setId(entity.getId())
                .setTitle(entity.getTitle())
                .setListPhotoVM(entity.getPhotos().stream()
                        .map(PhotoVM::fromEntity)
                        .toList())
                .setListIngredientVM(entity.getIngredients().stream()
                        .map(IngredientVM::fromEntity)
                        .toList())
                .setPreparationTime("%02d:%02d".formatted(
                        entity.getPreparationTime().toHoursPart(),
                        entity.getPreparationTime().toMinutesPart()
                ))
                .setCookingTime("%02d:%02d".formatted(
                        entity.getCookingTime().toHoursPart(),
                        entity.getCookingTime().toMinutesPart()
                ))
                .setServings(entity.getServings())
                .setLikes(entity.getLikes().stream()
                        .map(UserProfileVM::fromEntity)
                        .toList())
                .setLiked(Boolean.FALSE)
                .setDescription(entity.getDescription())
                .setAuthor(UserProfileVM.fromEntity(entity.getAuthor()))
                .setVisibilityStatus(entity.getVisibilityStatus())
                .setAddedOn(entity.getAddedOn())
                .setLastUpdated(entity.getLastUpdated());
    }

    public RecipeVM setLiked (boolean liked) {

        isLiked = liked;
        return this;
    }
}
