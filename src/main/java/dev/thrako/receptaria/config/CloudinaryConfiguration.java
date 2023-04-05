package dev.thrako.receptaria.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfiguration {

    @Value("#{environment.getProperty('CLOUDINARY_URL')}")
    String cloudinaryUrl;

    @Bean
    public Cloudinary getCloudinary () {


//        final Cloudinary cloudinary = new Cloudinary(System.getenv("CLOUDINARY_URL"));
        final Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
        cloudinary.config.secure = true;
        return cloudinary;
    }
}
