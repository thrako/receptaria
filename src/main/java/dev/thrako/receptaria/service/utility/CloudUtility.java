package dev.thrako.receptaria.service.utility;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dev.thrako.receptaria.error.exception.CloudException;
import dev.thrako.receptaria.model.entity.photo.dto.PhotoDTO;
import dev.thrako.receptaria.model.entity.photo.dto.PhotoBM;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class CloudUtility {

    public static final String TEMP_FOLDER_NAME = "temp";
    private final Cloudinary cloudinary;

    public CloudUtility (Cloudinary cloudinary) {

        this.cloudinary = cloudinary;
    }

    public void delete (String publicId) throws IOException {

        this.cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public PhotoDTO saveTemporary (PhotoBM photoBM) {

        try {
            //noinspection unchecked
            final Map<String, String> uploadMap = (Map<String, String>) cloudinary
                    .uploader()
                    .upload(photoBM.getFileData().getBytes(),
                            ObjectUtils.asMap("folder", TEMP_FOLDER_NAME));

            return PhotoDTO.fromBM(photoBM)
                    .setPublicId(uploadMap.get("public_id"))
                    .setUrl(uploadMap.get("url"));

        } catch (IOException e) {
            throw new CloudException(e);
        }
    }

    public List<PhotoDTO> moveAll (List<PhotoDTO> photoDTOList, Long recipeId) {

        final Function<PhotoDTO, PhotoDTO> moveToPermanentFolder = dto -> {
            try {
                //noinspection unchecked
                final Map<String, String> renamedMap = this.cloudinary
                        .uploader()
                        .rename(dto.getPublicId(),
                                "recipes/%d/%s".formatted(recipeId, dto.getPublicId()),
                                ObjectUtils.emptyMap());
                return dto
                        .setPublicId(renamedMap.get("public_id"))
                        .setUrl(renamedMap.get("url"));

            } catch (IOException e) {
                throw new CloudException(e);
            }
        };

        return photoDTOList.stream()
                .map(moveToPermanentFolder)
                .toList();
    }


}
