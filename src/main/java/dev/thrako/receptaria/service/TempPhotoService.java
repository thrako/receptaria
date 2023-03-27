package dev.thrako.receptaria.service;

import dev.thrako.receptaria.repository.TempPhotoRepository;
import org.springframework.stereotype.Service;

@Service
public class TempPhotoService {

    private final TempPhotoRepository tempPhotoRepository;

    public TempPhotoService (TempPhotoRepository tempPhotoRepository) {

        this.tempPhotoRepository = tempPhotoRepository;
    }


}
