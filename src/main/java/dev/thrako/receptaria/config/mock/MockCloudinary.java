package dev.thrako.receptaria.config.mock;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;

public class MockCloudinary extends Cloudinary {

    public Uploader uploader;

    public MockCloudinary () {

        this.uploader = new MockUploader(this);
    }

    @Override
    public Uploader uploader() {

        return this.uploader;
    }


}
