package dev.thrako.receptaria.model.recipe.dto;

import dev.thrako.receptaria.model.photo.PhotoEntity;
import dev.thrako.receptaria.model.recipe.RecipeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Accessors(chain = true)
public class RecipeCardDTO {

    private Long entityId;

    private String title;

    private String authorName;

    private String lastUpdated;

    private String coverPhotoUrl;

    private boolean canSee;

    private boolean canEdit;

    public static RecipeCardDTO fromEntity (RecipeEntity entity) {

        final DateTimeFormatter date = DateTimeFormatter.ofPattern("dd MMM yyyy");
        final DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm");
        final LocalDateTime entityLastUpdated = entity.getLastUpdated();

        return new RecipeCardDTO()
                .setEntityId(entity.getId())
                .setTitle(entity.getTitle())
                .setAuthorName(entity.getAuthor().getDisplayName())
                .setLastUpdated("%s, %s at %sh".formatted(
                        entityLastUpdated.getDayOfWeek().toString(),
                        entityLastUpdated.format(date),
                        entityLastUpdated.format(time))
                )
                .setCoverPhotoUrl(entity.getPhotos().isEmpty()
                        ? "/images/system/no_photo.webp"
                        : entity.getPhotos().stream()
                            .filter(PhotoEntity::isPrimary)
                            .findFirst()
                            .orElse(entity.getPhotos().get(0)).getUrl())
                ;
    }

}
