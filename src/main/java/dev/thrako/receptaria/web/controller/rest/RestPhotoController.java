package dev.thrako.receptaria.web.controller.rest;

import dev.thrako.receptaria.constant.CustomErrors;
import dev.thrako.receptaria.error.exception.CloudException;
import dev.thrako.receptaria.error.exception.NoSuchTempPhotoException;
import dev.thrako.receptaria.model.entity.photo.dto.PhotoBM;
import dev.thrako.receptaria.model.entity.photo.dto.PhotoVM;
import dev.thrako.receptaria.service.PhotoService;
import dev.thrako.receptaria.error.message.ErrorMessage;
import dev.thrako.receptaria.service.TempPhotoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/photos")
public class RestPhotoController {


    private final PhotoService photoService;
    private final TempPhotoService tempPhotoService;


    public RestPhotoController (PhotoService photoService,
                                TempPhotoService tempPhotoService) {

        this.photoService = photoService;
        this.tempPhotoService = tempPhotoService;
    }

    @PostMapping(path = "/temp/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhotoVM> upload (@Valid PhotoBM photoBM) {

        final PhotoVM photoVM = this.tempPhotoService.save(photoBM);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(photoVM);
    }

    @DeleteMapping("/temp/delete/{id}")
    public ResponseEntity<Object> delete (@PathVariable Long id) {

        try {
            this.tempPhotoService.delete(id);

            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();

        } catch (NoSuchTempPhotoException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ErrorMessage.from(e.getMessage()));

        } catch (CloudException e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomErrors.INTERNAL_SERVER_ERROR.getErrorMessage());
        }
    }

}

