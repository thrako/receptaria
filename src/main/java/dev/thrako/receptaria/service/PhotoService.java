package dev.thrako.receptaria.service;

import dev.thrako.receptaria.model.photo.dto.PhotoDTO;
import dev.thrako.receptaria.repository.PhotoRepository;
import dev.thrako.receptaria.utilities.UploadUtility;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoService (PhotoRepository photoRepository) {

        this.photoRepository = photoRepository;
    }


    public List<PhotoDTO> getPhotoDtoList (Collection<MultipartFile> multipartFiles) {

        return UploadUtility.getDTOList(multipartFiles);

    }


}
