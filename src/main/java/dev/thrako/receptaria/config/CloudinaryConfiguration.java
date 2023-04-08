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
    @ConditionalOnExpression("${cloud.service.mocked} == false")
    public Cloudinary cloudinary () {

        final Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
        cloudinary.config.secure = true;
        return cloudinary;
    }

    @Bean
    @ConditionalOnExpression("${cloud.service.mocked} == true")
    public Cloudinary mockCloudinary () {

        System.out.println("\u001B[32m \033[40m ---=== SORRY FOR MOCKING YOU, Cloudinary! ===--- by thrako with love\u001B[0m");

        return new MockCloudinary();
    }


}
