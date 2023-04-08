package dev.thrako.receptaria.model.entity.recipe.dto;

import dev.thrako.receptaria.model.entity.photo.PhotoEntity;
import dev.thrako.receptaria.model.entity.recipe.RecipeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class RecipeCardVM {

    private Long entityId;

    private String title;

    private String authorName;

    private Long authorId;

    private LocalDateTime lastUpdated;

    private String coverPhotoUrl;

    public static RecipeCardVM fromEntity (RecipeEntity entity) {

        return new RecipeCardVM()
                .setEntityId(entity.getId())
                .setTitle(entity.getTitle())
                .setAuthorName(entity.getAuthor().getDisplayName())
                .setAuthorId(entity.getAuthor().getId())
                .setLastUpdated(entity.getLastUpdated())
                .setCoverPhotoUrl(entity.getPhotos().isEmpty()
                        ? "/images/system/no_photo.webp"
                        : entity.getPhotos().stream()
                        .filter(PhotoEntity::isPrimary)
                        .findFirst()
                        .orElse(entity.getPhotos().get(0)).getUrl())
                ;
    }

}
