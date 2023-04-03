package dev.thrako.receptaria.service;

import dev.thrako.receptaria.model.repository.UserRepository;
import dev.thrako.receptaria.common.security.CurrentUser;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static dev.thrako.receptaria.common.constant.Constants.FORMAT_USER_WITH_EMAIL_NOT_FOUND;


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
                .map(CurrentUser::fromEntity)
                .orElseThrow(() -> getUserNotFoundException(email));
    }

    private UsernameNotFoundException getUserNotFoundException (String email) {

        return new UsernameNotFoundException(FORMAT_USER_WITH_EMAIL_NOT_FOUND.formatted(email));
    }
}
