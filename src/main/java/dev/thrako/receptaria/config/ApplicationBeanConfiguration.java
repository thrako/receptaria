package dev.thrako.receptaria.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    @Qualifier("userMapper")
    public ModelMapper userMapper () {

        return new ModelMapper();
    }



}
