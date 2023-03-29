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
public class PhotoVM {

    private Long id;

    private boolean isPrimary;

    private String url;

    private String description;

    private String filename;

    private UUID tempRecipeId;

    public static PhotoVM fromTempEntity (TempPhotoEntity tempPhotoEntity) {

        return new PhotoVM()
                .setId(tempPhotoEntity.getId())
                .setPrimary(tempPhotoEntity.isPrimary())
                .setUrl(tempPhotoEntity.getUrl())
                .setDescription(tempPhotoEntity.getDescription())
                .setFilename(tempPhotoEntity.getFilename())
                .setTempRecipeId(tempPhotoEntity.getTempRecipeId());
    }
}
