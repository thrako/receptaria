package dev.thrako.receptaria.controller.rest;

import dev.thrako.receptaria.constant.CustomErrors;
import dev.thrako.receptaria.exception.CloudException;
import dev.thrako.receptaria.exception.NoSuchTempPhotoException;
import dev.thrako.receptaria.model.photo.dto.PhotoBM;
import dev.thrako.receptaria.model.photo.dto.SavedPhotoDTO;
import dev.thrako.receptaria.service.PhotoService;
import dev.thrako.receptaria.utility.ErrorMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/photos")
public class RestPhotoController {


    private final PhotoService photoService;


    public RestPhotoController (PhotoService photoService) {

        this.photoService = photoService;
    }

    @PostMapping(path = "/temp/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SavedPhotoDTO> upload (@Valid PhotoBM photoBM) {

        final SavedPhotoDTO savedPhotoDTO = this.photoService.save(photoBM);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPhotoDTO);
    }

    @DeleteMapping("/temp/delete/{id}")
    public ResponseEntity<Object> delete (@PathVariable Long id) {

        try {
            this.photoService.delete(id);

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

