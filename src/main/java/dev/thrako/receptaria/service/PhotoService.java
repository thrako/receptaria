package dev.thrako.receptaria.service;

import dev.thrako.receptaria.model.photo.dto.PhotoUploadDTO;
import dev.thrako.receptaria.repository.PhotoRepository;
import dev.thrako.receptaria.utility.UploadUtility;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoService (PhotoRepository photoRepository) {

        this.photoRepository = photoRepository;
    }


    public List<PhotoUploadDTO> getPhotoDtoList (Collection<MultipartFile> multipartFiles) {

        try {
            return UploadUtility.getDTOList(multipartFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
