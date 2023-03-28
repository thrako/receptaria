package dev.thrako.receptaria.service;

import dev.thrako.receptaria.model.photo.dto.SavedPhotoDTO;
import dev.thrako.receptaria.repository.TempPhotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
public class TempPhotoService {

    private final TempPhotoRepository tempPhotoRepository;

    public TempPhotoService (TempPhotoRepository tempPhotoRepository) {

        this.tempPhotoRepository = tempPhotoRepository;
    }

    public List<SavedPhotoDTO> getSavedPhotos (UUID tempRecipeId, Long primaryPhotoId) {

        final Function<SavedPhotoDTO, SavedPhotoDTO> checkPrimary =
                dto -> dto.setPrimary(primaryPhotoId.equals(dto.getId()));

        return this.tempPhotoRepository.findAllByRecipeBMId(tempRecipeId)
                .stream()
                .map(SavedPhotoDTO::fromTempEntity)
                .map(checkPrimary)
                .toList();
    }


}
