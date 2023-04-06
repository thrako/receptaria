package dev.thrako.receptaria.config.mock;

import com.cloudinary.Cloudinary;
import com.cloudinary.ProgressCallback;
import com.cloudinary.Uploader;
import com.cloudinary.strategies.AbstractUploaderStrategy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MockUploader extends Uploader {

    Cloudinary cloudinary;
    Map<String, String> uploaderMap;

    public MockUploader (Cloudinary cloudinary, AbstractUploaderStrategy ignored) {

        super(cloudinary, new AbstractUploaderStrategy() {
            @Override
            public Map callApi (String action,
                                Map<String, Object> params,
                                Map options,
                                Object file,
                                ProgressCallback progressCallback) throws IOException {

                return null;
            }
        });
    }

    public MockUploader(MockCloudinary cloudinary) {

        this(null, null);
        this.uploaderMap = new HashMap<>();
        this.cloudinary = cloudinary;
    }

    @Override
    public Map upload(Object ignoredOne, Map ignoredTwo) {

        final String publicId = UUID.randomUUID().toString();

        this.uploaderMap.put(publicId, "https://picsum.photos/200");

        final HashMap<String, String> map = new HashMap<>();
        map.put("public_id", publicId);
        map.put("url", "https://picsum.photos/200");

        return map;
    }

    @Override
    public Map rename(String fromPublicId, String toPublicId, Map options) {

        this.uploaderMap.put(toPublicId, "https://picsum.photos/200");

        final HashMap<String, String> map = new HashMap<>();
        map.put("public_id", toPublicId);
        map.put("url", "https://picsum.photos/200");

        return map;
    }

    @Override
    public Map destroy(String publicId, Map ignored) {

        this.uploaderMap.remove(publicId);

        final HashMap<String, String> map = new HashMap<>();
        map.put("status", "ok");

        return map;
    }
}