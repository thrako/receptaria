package dev.thrako.receptaria.service;

import dev.thrako.receptaria.constant.Role;
import dev.thrako.receptaria.error.exception.UserNotFoundException;
import dev.thrako.receptaria.model.entity.user.UserEntity;
import dev.thrako.receptaria.model.entity.user.dto.UserProfileVM;
import dev.thrako.receptaria.model.entity.user.dto.UserRegistrationBM;
import dev.thrako.receptaria.model.repository.RecipeRepository;
import dev.thrako.receptaria.model.repository.RoleRepository;
import dev.thrako.receptaria.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RecipeRepository recipeRepository;
    private final ModelMapper userMapper;
    private final PasswordEncoder encoder;

    public UserService (UserRepository userRepository,
                        RoleRepository roleRepository,
                        RecipeRepository recipeRepository,
                        @Qualifier("userMapper") ModelMapper userMapper,
                        PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.recipeRepository = recipeRepository;
        this.userMapper = userMapper;
        this.encoder = passwordEncoder;
    }

    public void register (UserRegistrationBM userDTO) {

        UserEntity userEntity = this.userMapper.map(userDTO, UserEntity.class);
        userEntity.setPassword(encoder.encode(userDTO.getPassword()));
        userEntity.setActive(true);
        userEntity.addRoles(roleRepository.getByRole(Role.USER));

        this.userRepository.saveAndFlush(userEntity);
    }

    public UserProfileVM getUserProfileById (Long id) {

        return this.findUserById(id).map(UserProfileVM::fromEntity)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found!"));
    }

    public Optional<UserEntity> findUserById (Long id) {

        return this.userRepository.findUserById(id);
    }

    public boolean existsByEmail (String email) {

        return this.userRepository.existsByEmail(email);
    }

    public UserEntity getUserEntity (Long id) {

        return this.userRepository.getUserEntityById(id);

    }

    public String getDisplayNameById (Long id) {

        return this.findUserById(id)
                .map(UserEntity::getDisplayName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user exists!"));
    }

}
