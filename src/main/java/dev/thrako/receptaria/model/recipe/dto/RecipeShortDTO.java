package dev.thrako.receptaria.model.recipe.dto;

import dev.thrako.receptaria.model.recipe.RecipeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RecipeShortDTO {

    private Long entityId;

    private String title;

    private String authorName;

    private String lastUpdated;

    private String coverPhotoPath;

    public static RecipeShortDTO fromEntity (RecipeEntity entity) {
        return new RecipeShortDTO()
                .setEntityId(entity.getId())
                .setTitle(entity.getTitle())
                .setAuthorName(entity.getAuthor().getDisplayName())
                .setLastUpdated(entity.getLastUpdated().toString())
                .setCoverPhotoPath(entity.getPhotos().isEmpty()
                        ? "/images/uploads/no_photo.webp"
                        : entity.getPhotos().get(0).getUrl().replace("src/main/resources/static", ""));
    }

}
