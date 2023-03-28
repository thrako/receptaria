package dev.thrako.receptaria.service;

import dev.thrako.receptaria.exception.CloudException;
import dev.thrako.receptaria.exception.NoSuchTempPhotoException;
import dev.thrako.receptaria.model.photo.TempPhotoEntity;
import dev.thrako.receptaria.model.photo.dto.UploadedPhotoDTO;
import dev.thrako.receptaria.model.photo.dto.PhotoBM;
import dev.thrako.receptaria.model.photo.dto.SavedPhotoDTO;
import dev.thrako.receptaria.repository.PhotoRepository;
import dev.thrako.receptaria.repository.TempPhotoRepository;
import dev.thrako.receptaria.utility.CloudUtility;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import static dev.thrako.receptaria.constant.Constants.FORMAT_NO_SUCH_TEMP_PHOTO;

@Service
public class PhotoService {


    private final PhotoRepository photoRepository;
    private final TempPhotoRepository tempPhotoRepository;
    private final CloudUtility cloudUtility;

    public PhotoService (PhotoRepository photoRepository,
                         TempPhotoRepository tempPhotoRepository,
                         CloudUtility cloudUtility) {

        this.photoRepository = photoRepository;
        this.tempPhotoRepository = tempPhotoRepository;
        this.cloudUtility = cloudUtility;
    }


    public SavedPhotoDTO save (PhotoBM photoBM) {

        final UploadedPhotoDTO uploadedPhotoDTO = this.cloudUtility.saveTemporary(photoBM);
        final TempPhotoEntity tempPhotoEntity =
                this.tempPhotoRepository.saveAndFlush(uploadedPhotoDTO.toEntity());

        return SavedPhotoDTO.fromTempEntity(tempPhotoEntity);
    }

    public void delete (Long id) {

        final Optional<TempPhotoEntity> optionalTempPhoto = this.tempPhotoRepository.findById(id);
        final TempPhotoEntity tempPhotoEntity = optionalTempPhoto
                .orElseThrow(() -> new NoSuchTempPhotoException(FORMAT_NO_SUCH_TEMP_PHOTO.formatted(id)));
        try {
            this.cloudUtility.delete(tempPhotoEntity.getPublicId()) ;
            this.tempPhotoRepository.delete(tempPhotoEntity);
        } catch (IOException e) {
            throw new CloudException(e);
        }

    }
}
