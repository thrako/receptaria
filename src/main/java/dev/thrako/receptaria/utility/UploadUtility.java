package dev.thrako.receptaria.utility;

import dev.thrako.receptaria.exception.NotSupportedMediaTypeException;
import dev.thrako.receptaria.model.photo.dto.PhotoUploadDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import static dev.thrako.receptaria.constant.Constants.UPLOAD_DIR;

public class UploadUtility {

    public static final String FILE_SEPARATOR = ".";
    public static final String DATE_FORMAT = "yyyyMMdd-HHmmss";


    private UploadUtility () {

        throw new UnsupportedOperationException();
    }

    public static String getDateTimePrefixedURL (MultipartFile multipartFile) {

        String originalFilename = Optional.ofNullable(multipartFile.getOriginalFilename()).orElse("unnamed");

        String photoExtension = PhotoExtensionValidator.getDetectedExtension(multipartFile);
        String exMessage = "Not supported media type of file: " + originalFilename;
        String extension = Optional.ofNullable(photoExtension)
                .orElseThrow(() -> new NotSupportedMediaTypeException(exMessage));

        int endIndex = originalFilename.lastIndexOf(FILE_SEPARATOR);
        String filename = originalFilename.substring(0, endIndex);


        String dateTimePrefixedName = new SimpleDateFormat(DATE_FORMAT + "'_" + filename + extension + "'")
                .format(new Date());

        return UPLOAD_DIR + dateTimePrefixedName;
    }

    public static List<PhotoUploadDTO> getDTOList (Collection<MultipartFile> multipartFiles) throws IOException {

        List<PhotoUploadDTO> photoDTOS = new ArrayList<>();

        for (var multipartFile : multipartFiles) {

            if (multipartFile.isEmpty()) {
                continue;
            }

            String url;
            try {
                url = UploadUtility.getDateTimePrefixedURL(multipartFile);
            } catch (NotSupportedMediaTypeException e) {
                System.out.println(e.getMessage());
                continue;
            }

            final PhotoUploadDTO photoDTO = new PhotoUploadDTO(url, multipartFile);
            photoDTOS.add(photoDTO);
        }

        return photoDTOS;
    }

    public static boolean uploadPhoto(PhotoUploadDTO photoDTO) {

        final Path saveToPath = Paths.get(photoDTO.getUrl());
        final MultipartFile multipartFile = photoDTO.getMultipartFile();

        try (final InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, saveToPath);
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

}
