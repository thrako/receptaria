package dev.thrako.receptaria.config;

import dev.thrako.receptaria.user.UserEntity;
import dev.thrako.receptaria.user.dto.UserLoginDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }


    @Bean
    public ModelMapper modelMapper(PasswordEncoder passwordEncoder){
        ModelMapper mapper = new ModelMapper();

        Converter<String, String> passwordHash = ctx -> ctx.getSource() == null ? null :
                        passwordEncoder.encode(ctx.getSource());


        //encode password on the fly
        mapper.createTypeMap(UserLoginDTO.class, UserEntity.class)
                .addMappings(mpr -> mpr.using(passwordHash)
                        .map(UserLoginDTO::getPassword, UserEntity::setPassword));


        return mapper;
    }

}
