package dev.thrako.receptaria.utility;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dev.thrako.receptaria.model.photo.dto.UploadedPhotoDTO;
import dev.thrako.receptaria.model.photo.dto.PhotoBM;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

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

    public UploadedPhotoDTO saveTemporary (PhotoBM photoBM) {

        try {
            //noinspection unchecked
            final Map<String, String> uploadMap = (Map<String, String>) cloudinary
                    .uploader()
                    .upload(photoBM.getFileData().getBytes(),
                            ObjectUtils.asMap("folder", TEMP_FOLDER_NAME));

            return UploadedPhotoDTO.fromBM(photoBM)
                    .setPublicId(uploadMap.get("public_id"))
                    .setUrl(uploadMap.get("url"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
