package dev.thrako.receptaria.utilities;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

public class PhotoExtensionValidator {

    private static final Tika TIKA = new Tika();
    private static final HashMap<String, String> ALLOWED_TYPES_EXTENSIONS = new HashMap<>();

    static {
        ALLOWED_TYPES_EXTENSIONS.put("image/png", ".png");
        ALLOWED_TYPES_EXTENSIONS.put("image/apng", ".apng");
        ALLOWED_TYPES_EXTENSIONS.put("image/bmp", ".bmp");
        ALLOWED_TYPES_EXTENSIONS.put("image/jpeg", ".jpeg");
        ALLOWED_TYPES_EXTENSIONS.put("image/gif", ".gif");
        ALLOWED_TYPES_EXTENSIONS.put("image/avif", ".avif");
        ALLOWED_TYPES_EXTENSIONS.put("image/tiff", ".tiff");
        ALLOWED_TYPES_EXTENSIONS.put("image/svg+xml", ".svg");
        ALLOWED_TYPES_EXTENSIONS.put("image/webp", ".webp");
    }

    private PhotoExtensionValidator () {

        throw new UnsupportedOperationException();
    }
    
    public static String getDetectedExtension (MultipartFile multipartFile) {

        String detectedType = getDetectedType(multipartFile);

        return ALLOWED_TYPES_EXTENSIONS.get(detectedType);
    }

    private static String getDetectedType (MultipartFile multipartFile) {

        String detectedType;
        try {
            detectedType = TIKA.detect(multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return detectedType;
    }

    public static boolean isAllowedType (MultipartFile multipartFile) {
        return getDetectedExtension(multipartFile) != null;
    }

    public static boolean isAnImage (MultipartFile multipartFile) {

        return getDetectedType(multipartFile).startsWith("image/");
    }
}
