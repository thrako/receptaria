package dev.thrako.receptaria.config;

import dev.thrako.receptaria.service.utility.ContextAuthChecker;
import dev.thrako.receptaria.web.interceptor.ContextAuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final ContextAuthChecker contextAuthChecker;

    @Autowired
    public WebConfiguration (ContextAuthChecker contextAuthChecker) {

        this.contextAuthChecker = contextAuthChecker;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        final ContextAuthorizationInterceptor contextAuthorizationInterceptor
                = new ContextAuthorizationInterceptor(contextAuthChecker);

        registry.addInterceptor(contextAuthorizationInterceptor).addPathPatterns("/recipes/{id}");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
