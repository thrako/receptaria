package dev.thrako.receptaria.service;

import dev.thrako.receptaria.constant.Role;
import dev.thrako.receptaria.exception.UserNotFoundException;
import dev.thrako.receptaria.model.recipe.dto.RecipeCardDTO;
import dev.thrako.receptaria.model.user.UserEntity;
import dev.thrako.receptaria.model.user.dto.UserProfileDTO;
import dev.thrako.receptaria.model.user.dto.UserRegistrationBM;
import dev.thrako.receptaria.repository.RecipeRepository;
import dev.thrako.receptaria.repository.RoleRepository;
import dev.thrako.receptaria.repository.UserRepository;
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

    public UserProfileDTO getUserProfileById (Long id) {

        return this.findUserById(id).map(this::mapToProfileDTO)
                //TODO Make custom Exception
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found!"));
    }

    public Optional<UserEntity> findUserById (Long id) {

        return this.userRepository.findUserById(id);
    }

    private UserProfileDTO mapToProfileDTO (UserEntity entity) {

        return new UserProfileDTO()
                .setDisplayName(entity.getDisplayName())
                .setActive(entity.isActive())
                .setUserRecipes(recipeRepository.findByAuthor_Id(entity.getId())
                        .stream()
                        .map(RecipeCardDTO::fromEntity)
                        .toList());
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
