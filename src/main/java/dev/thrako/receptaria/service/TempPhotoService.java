package dev.thrako.receptaria.service;

import dev.thrako.receptaria.error.exception.CloudException;
import dev.thrako.receptaria.error.exception.NoSuchTempPhotoException;
import dev.thrako.receptaria.model.entity.photo.TempPhotoEntity;
import dev.thrako.receptaria.model.entity.photo.dto.PhotoBM;
import dev.thrako.receptaria.model.entity.photo.dto.PhotoDTO;
import dev.thrako.receptaria.model.entity.photo.dto.PhotoVM;
import dev.thrako.receptaria.model.repository.TempPhotoRepository;
import dev.thrako.receptaria.service.utility.CloudUtility;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static dev.thrako.receptaria.constant.Constants.FORMAT_NO_SUCH_TEMP_PHOTO;

@Service
public class TempPhotoService {

    private final TempPhotoRepository tempPhotoRepository;
    private final CloudUtility cloudUtility;

    public TempPhotoService (TempPhotoRepository tempPhotoRepository,
                             CloudUtility cloudUtility) {

        this.tempPhotoRepository = tempPhotoRepository;
        this.cloudUtility = cloudUtility;
    }

    public PhotoVM save (PhotoBM photoBM) {

        final TempPhotoEntity newEntity = this.cloudUtility
                .saveTemporary(photoBM)
                .toTempEntity();

        final TempPhotoEntity savedEntity = this.tempPhotoRepository
                .saveAndFlush(newEntity);

        return PhotoVM
                .fromTempEntity(savedEntity);
    }

    public void updatePrimaryFlag (UUID tempRecipeId, Long primaryPhotoId) {

        final Function<TempPhotoEntity, TempPhotoEntity> setPrimaryFunction =
                entity -> entity.setPrimary(Objects.equals(primaryPhotoId, entity.getId()));

        final List<TempPhotoEntity> tempPhotoEntities = getAllByTempRecipeId(tempRecipeId)
                    .stream()
                    .map(setPrimaryFunction)
                    .toList();

        final long primaryPhotosCount = tempPhotoEntities.stream()
                .filter(TempPhotoEntity::isPrimary)
                .count();

        if (primaryPhotosCount != 1) {
            tempPhotoEntities.stream()
                    .map(tempPhotoEntity -> tempPhotoEntity.setPrimary(Boolean.FALSE))
                    .findFirst()
                    .ifPresent(savedPhotoDTO -> savedPhotoDTO.setPrimary(Boolean.TRUE));
        }

        this.tempPhotoRepository.saveAllAndFlush(tempPhotoEntities);
    }

    public List<PhotoVM> getListPhotoVM (UUID tempRecipeId) {

        return getAllByTempRecipeId(tempRecipeId)
                .stream()
                .map(PhotoVM::fromTempEntity)
                .toList();
    }

    public List<PhotoDTO> getListPhotoDTO (UUID tempRecipeId) {

        return getAllByTempRecipeId(tempRecipeId)
                .stream()
                .map(PhotoDTO::fromTempEntity)
                .toList();
    }

    private List<TempPhotoEntity> getAllByTempRecipeId (UUID tempRecipeId) {

        return this.tempPhotoRepository.findAllByTempRecipeId(tempRecipeId);
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
