package dev.thrako.receptaria.service;

import dev.thrako.receptaria.constant.Role;
import dev.thrako.receptaria.model.recipe.dto.RecipeShortDTO;
import dev.thrako.receptaria.model.user.UserEntity;
import dev.thrako.receptaria.model.user.dto.UserProfileDTO;
import dev.thrako.receptaria.model.user.dto.UserRegistrationBM;
import dev.thrako.receptaria.repository.RecipeRepository;
import dev.thrako.receptaria.repository.RoleRepository;
import dev.thrako.receptaria.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;

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


    public UserEntity getPrincipalEntity (String email) {

        return this.userRepository.getUserByEmail(email);
    }

    public Long getPrincipalId (Principal principal) {

        return this.getPrincipalEntity(principal).getId();
    }

    public UserEntity getPrincipalEntity (Principal principal) {

        return this.userRepository.getUserByEmail(principal.getName());
    }

    public boolean register (UserRegistrationBM userDTO) {

        UserEntity userEntity = this.userMapper.map(userDTO, UserEntity.class);
        userEntity.setPassword(encoder.encode(userDTO.getPassword()));
        userEntity.setActive(true);
        userEntity.addRoles(roleRepository.getByRole(Role.USER));
        this.userRepository.saveAndFlush(userEntity);

        return this.userRepository
                .findUserByEmail(userEntity.getEmail())
                .isPresent();
    }

    public UserProfileDTO getUserProfileById (Long id) {

        return this.findUserById(id).map(this::mapToProfileDTO)
                //TODO Make custom Exception
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found!"));
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
                        .map(recipe -> new RecipeShortDTO()
                                        .setEntityId(recipe.getId())
                                        .setTitle(recipe.getTitle())
//                                .setCoverPhoto(recipe.getPhotos()
//                                        .stream()
//                                        .findFirst()
//                                        .map(photo -> new SavedPhotoDTO(new File(photo.getUrl()))).orElse(null))
                        ).collect(Collectors.toSet()));
    }

    public boolean existsByEmail (String email) {

        return this.userRepository.existsByEmail(email);
    }
}
