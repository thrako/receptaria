package dev.thrako.receptaria.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfiguration {

//    private final String api_key;
//    private final String api_secret;
//    private final String cloudinaryUrl;


    public CloudinaryConfiguration (Environment environment) {
//        this.api_key = environment.getProperty("CLOUDINARY_API_KEY");
//        this.api_secret = environment.getProperty("CLOUDINARY_API_SECRET");

//        this.cloudinaryUrl = environment.getProperty("CLOUDINARY_URL");
    }

    @Bean
    public Cloudinary getCloudinary () {
//        Map<String, String> config = new HashMap<>();
//        config.put("cloud_name", "receptaria");
//        config.put("api_key", api_key); //
//        config.put("api_secret", api_secret);
        final Cloudinary cloudinary = new Cloudinary(System.getenv("CLOUDINARY_URL"));
        cloudinary.config.secure = true;
        return cloudinary;
    }
}
