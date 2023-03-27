package dev.thrako.receptaria.model.photo.dto;

import dev.thrako.receptaria.model.photo.TempPhotoEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Setter
@Getter
@Accessors(chain = true)
public class UploadedPhotoDTO {

    private String publicId;

    private String url;

    private String description;

    private String filename;

    private UUID recipeBMId;

    public static UploadedPhotoDTO fromBM (PhotoBM photoBM) {

        return new UploadedPhotoDTO()
                .setDescription(photoBM.getDescription())
                .setFilename(photoBM.getFileData().getOriginalFilename())
                .setRecipeBMId(photoBM.getRecipeBMId());
    }

    public TempPhotoEntity toEntity () {

        return new TempPhotoEntity()
                .setPublicId(this.getPublicId())
                .setPrimary(Boolean.FALSE)
                .setUrl(this.getUrl())
                .setDescription(this.getDescription())
                .setFilename(this.getFilename())
                .setRecipeBMId(this.getRecipeBMId());
    }
}
