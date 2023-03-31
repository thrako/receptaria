package dev.thrako.receptaria.service;

import dev.thrako.receptaria.model.entity.photo.PhotoEntity;
import dev.thrako.receptaria.model.repository.PhotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoService {


    private final PhotoRepository photoRepository;

    public PhotoService (PhotoRepository photoRepository) {

        this.photoRepository = photoRepository;
    }


    public List<PhotoEntity> saveAllAndFlush (List<PhotoEntity> photoEntities) {

        return this.photoRepository.saveAllAndFlush(photoEntities);
    }
}
