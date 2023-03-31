package dev.thrako.receptaria.model.entity.photo.dto;

import dev.thrako.receptaria.model.entity.photo.PhotoEntity;
import dev.thrako.receptaria.model.entity.photo.TempPhotoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class PhotoDTO {

   private Long id;

    private Long tempId;

    private String publicId;

    private String url;

    private String description;

    private String filename;

    private boolean isPrimary;

    private Long recipeId;

    private UUID tempRecipeId;

    public static PhotoDTO fromTempEntity (TempPhotoEntity tempPhotoEntity) {

        return new PhotoDTO()
                .setTempId(tempPhotoEntity.getId())
                .setPublicId(tempPhotoEntity.getPublicId())
                .setUrl(tempPhotoEntity.getUrl())
                .setDescription(tempPhotoEntity.getDescription())
                .setFilename(tempPhotoEntity.getFilename())
                .setPrimary(tempPhotoEntity.isPrimary())
                .setTempRecipeId(tempPhotoEntity.getTempRecipeId());
    }

    public static PhotoDTO fromBM (PhotoBM photoBM) {

        return new PhotoDTO()
                .setDescription(photoBM.getDescription())
                .setFilename(photoBM.getFileData().getOriginalFilename())
                .setPrimary(Boolean.FALSE)
                .setTempRecipeId(photoBM.getTempRecipeId());
    }

    public TempPhotoEntity toTempEntity () {

        return new TempPhotoEntity()
                .setId(this.getTempId())
                .setPublicId(this.getPublicId())
                .setUrl(this.getUrl())
                .setDescription(this.getDescription())
                .setFilename(this.getFilename())
                .setPrimary(this.isPrimary())
                .setTempRecipeId(this.getTempRecipeId());
    }

    public PhotoEntity toEntity () {

        return new PhotoEntity()
                .setId(this.getId())
                .setPublicId(this.getPublicId())
                .setUrl(this.getUrl())
                .setDescription(this.getDescription())
                .setFilename(this.getFilename())
                .setPrimary(this.isPrimary());
    }
}
