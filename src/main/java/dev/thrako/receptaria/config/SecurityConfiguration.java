package dev.thrako.receptaria.config;

import dev.thrako.receptaria.repository.UserRepository;
import dev.thrako.receptaria.service.AppUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception {

        httpSecurity
            .authorizeHttpRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/static/images/uploads/**").permitAll()
                .requestMatchers("/", "/login", "/registration", "/registration/success", "/test", "/api/test").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/users/me")
                    .failureForwardUrl("/login")
            .and()
                .logout().deleteCookies("JSESSIONID").clearAuthentication(true).invalidateHttpSession(true)
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
        ;

        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder () {

        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new AppUserDetailsService(userRepository);
    }

}
