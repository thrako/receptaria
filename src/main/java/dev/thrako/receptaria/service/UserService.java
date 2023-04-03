package dev.thrako.receptaria.service;

import dev.thrako.receptaria.common.constant.Role;
import dev.thrako.receptaria.common.error.exception.UserNotFoundException;
import dev.thrako.receptaria.model.entity.user.UserEntity;
import dev.thrako.receptaria.model.entity.user.dto.UserProfileVM;
import dev.thrako.receptaria.model.entity.user.dto.UserRegistrationBM;
import dev.thrako.receptaria.model.repository.RoleRepository;
import dev.thrako.receptaria.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper userMapper;
    private final PasswordEncoder encoder;

    public UserService (UserRepository userRepository,
                        RoleRepository roleRepository,
                        @Qualifier("userMapper") ModelMapper userMapper,
                        PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.encoder = passwordEncoder;
    }

    public void register (UserRegistrationBM userDTO) {

        UserEntity userEntity = this.userMapper.map(userDTO, UserEntity.class);
        userEntity.addRoles(roleRepository.getByRole(Role.USER));
        userEntity.setPassword(encoder.encode(userDTO.getPassword()));
        //TODO email activation
        userEntity.setActive(true);

        this.userRepository.saveAndFlush(userEntity);
    }

    public UserEntity findById (Long id) {

        return this.userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found!"));
    }

    public UserProfileVM findUserProfileById (Long id) {

        return UserProfileVM.fromEntity(this.findById(id));
    }

    public boolean existsByEmail (String email) {

        return this.userRepository.existsByEmail(email);
    }

    public String findDisplayNameById (Long id) {

        return this.findById(id).getDisplayName();
    }

    public void saveAndFlush (UserEntity userEntity) {

        this.userRepository.saveAndFlush(userEntity);
    }
}
