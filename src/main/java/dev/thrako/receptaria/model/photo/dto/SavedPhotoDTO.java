package dev.thrako.receptaria.model.photo.dto;

import dev.thrako.receptaria.model.photo.TempPhotoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class SavedPhotoDTO {

    private Long id;

    private boolean isPrimary;

    private String url;

    private String description;

    private String filename;

    private UUID recipeBMId;

    public static SavedPhotoDTO fromTempEntity (TempPhotoEntity tempPhotoEntity) {

        return new SavedPhotoDTO()
                .setId(tempPhotoEntity.getId())
                .setPrimary(tempPhotoEntity.isPrimary())
                .setUrl(tempPhotoEntity.getUrl())
                .setDescription(tempPhotoEntity.getDescription())
                .setFilename(tempPhotoEntity.getFilename())
                .setRecipeBMId(tempPhotoEntity.getRecipeBMId());
    }
}
