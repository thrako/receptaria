package dev.thrako.receptaria.config;

import dev.thrako.receptaria.model.entity.user.UserEntity;
import dev.thrako.receptaria.model.entity.user.dto.UserLoginBM;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    @Qualifier("userMapper")
    public ModelMapper userMapper (PasswordEncoder passwordEncoder) {

        ModelMapper mapper = new ModelMapper();

        Converter<String, String> passwordHash = ctx -> ctx.getSource() == null
                                                        ? null
                                                        : passwordEncoder.encode(ctx.getSource());

        mapper.createTypeMap(UserLoginBM.class, UserEntity.class)
                .addMappings(mpr -> mpr.using(passwordHash).map(UserLoginBM::getPassword, UserEntity::setPassword));

        return mapper;
    }

}
