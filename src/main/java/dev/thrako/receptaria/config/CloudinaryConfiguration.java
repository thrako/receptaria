package dev.thrako.receptaria.config;

import com.cloudinary.Cloudinary;
import dev.thrako.receptaria.config.mock.MockCloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfiguration {


    @Value("#{environment.getProperty('CLOUDINARY_URL')}")
    String cloudinaryUrl;

    @Bean
    @ConditionalOnExpression("${cloud.service.enabled} == true")
    public Cloudinary cloudinary () {

        final Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
        cloudinary.config.secure = true;
        return cloudinary;
    }

    @Bean
    @ConditionalOnExpression("${cloud.service.enabled} == false")
    public Cloudinary mockCloudinary () {

        System.out.println("|||---+++===Cloudinary, SORRY FOR MOCKING YOU!===+++---||| by thrako");

        return new MockCloudinary();
    }


}
