package dev.thrako.receptaria.user;

import dev.thrako.receptaria.constants.Roles;
import dev.thrako.receptaria.security.CurrentUser;
import dev.thrako.receptaria.user.dto.UserLoginDTO;
import dev.thrako.receptaria.user.dto.UserRegistrationDTO;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;

    private CurrentUser currentUser;

    public UserService (UserRepository userRepository,
                        ModelMapper mapper,
                        CurrentUser currentUser,
                        PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.mapper = mapper;
        this.currentUser = currentUser;
        this.encoder = passwordEncoder;
    }

    public boolean login (UserLoginDTO userDTO) {

        final String emailInput = userDTO.getEmail();
        final String passwordInput = userDTO.getPassword();

        final UserEntity userEntity = this.userRepository.findUserByEmail(emailInput).orElse(null);

        if (userEntity == null || !this.encoder.matches(passwordInput, userEntity.getPassword())) {
            return false;
        }

        setCurrentUser(userEntity);

        return true;
    }

    private void setCurrentUser (UserEntity userEntity) {

        this.currentUser.setId(userEntity.getId());
        this.currentUser.setEmail(userEntity.getEmail());
        this.currentUser.setDisplayName(userEntity.getDisplayName());
    }

    public void logout() {
        this.currentUser.clear();
    }

    public boolean isLoggedIn() {
        return currentUser.isLoggedIn();
    }

    public boolean register (UserRegistrationDTO userDTO) {

        UserEntity userEntity = this.mapper.map(userDTO, UserEntity.class);
        userEntity.setPassword(encoder.encode(userDTO.getPassword()));
        userEntity.setActive(true);
        userEntity.setRole(Roles.USER);
        this.userRepository.saveAndFlush(userEntity);

        return this.userRepository
                .findUserByEmail(userEntity.getEmail())
                .isPresent();
    }

    public Optional<UserEntity> findUserById (Long id) {

        return this.userRepository.findUserById(id);
    }

    public Optional<UserEntity> findById (Long id) {
        return this.userRepository.findById(id);
    }
}
