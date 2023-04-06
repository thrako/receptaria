package dev.thrako.receptaria.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cloud.service")
public class CloudProperties {

    /**
     * Enables instantiation of Cloudinary instance, MockCloudinary instance is instantiated otherwise.
     */
    private boolean enabled = true;


    public boolean getEnabled () {

        return enabled;
    }

    public void setEnabled (boolean enabled) {

        this.enabled = enabled;
    }
}

