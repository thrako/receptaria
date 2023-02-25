package dev.thrako.receptaria.service;

import dev.thrako.receptaria.model.user.UserEntity;
import dev.thrako.receptaria.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static dev.thrako.receptaria.constant.Constants.FORMAT_USER_WITH_EMAIL_NOT_FOUND;
import static dev.thrako.receptaria.constant.Constants.ROLE_PREFIX;


public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    public AppUserDetailsService (UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {

        return this.userRepository
                .findUserByEmail(email)
                    .map(this::mapToUserDetails)
                .orElseThrow(() -> getUserNotFoundException(email));
    }

    private UserDetails mapToUserDetails (UserEntity userEntity) {

        return User
                .withUsername(userEntity.getEmail())
                .password(userEntity.getPassword())
                .authorities(getRoles(userEntity))
                .build();
    }

    private String[] getRoles (UserEntity userEntity) {

        return userEntity
                .getRoles()
                .stream()
                .map(entity -> ROLE_PREFIX + entity.getRole().name())
                .toArray(String[]::new);
    }

    private UsernameNotFoundException getUserNotFoundException (String email) {

        return new UsernameNotFoundException(String.format(FORMAT_USER_WITH_EMAIL_NOT_FOUND, email));
    }
}
