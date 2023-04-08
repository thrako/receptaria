package dev.thrako.receptaria.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cloud.service")
public class CloudProperties {

    /**
     * Enables instantiation of MockCloudinary instance, for Cloudinary instance set property to false.
     */
    private boolean mocked = false;


    public boolean getMocked () {

        return mocked;
    }

    public void setMocked (boolean mocked) {

        this.mocked = mocked;
    }
}

