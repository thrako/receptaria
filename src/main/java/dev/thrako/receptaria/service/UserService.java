package dev.thrako.receptaria.service;

import dev.thrako.receptaria.common.constant.Role;
import dev.thrako.receptaria.common.error.exception.IllegalTargetException;
import dev.thrako.receptaria.common.error.exception.NotAuthorizedException;
import dev.thrako.receptaria.common.error.exception.UserNotFoundException;
import dev.thrako.receptaria.common.security.CurrentUser;
import dev.thrako.receptaria.model.entity.role.RoleEntity;
import dev.thrako.receptaria.model.entity.user.UserEntity;
import dev.thrako.receptaria.model.entity.user.dto.UserProfileVM;
import dev.thrako.receptaria.model.entity.user.dto.UserRegistrationBM;
import dev.thrako.receptaria.model.entity.user.dto.UserVM;
import dev.thrako.receptaria.model.repository.RoleRepository;
import dev.thrako.receptaria.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<UserVM> getListUserVM () {

        return this.userRepository.findAll()
                .stream()
                .map(UserVM::fromEntity)
                .toList();
    }

    public void activate (Long userId, CurrentUser requester) {

        ensureAdminOrModerator(requester);

        final UserEntity userEntity = this.findById(userId);

        if (userEntity.isActive()) {
            throw new IllegalStateException("Trying to inactivate inactive user!");
        }

        userEntity.setActive(Boolean.TRUE);

        this.userRepository.saveAndFlush(userEntity);
    }

    public void inactivate (Long userId, CurrentUser requester) {


        ensureAdminOrModerator(requester);

        final UserEntity userEntity = this.findById(userId);

        if (!userEntity.isActive()) {
            throw new IllegalStateException("Trying to inactivate inactive user!");
        }

        if (hasRole(userEntity, Role.ADMIN)) {
            throw new IllegalTargetException("Operation not allowed for target: admin");
        }

        userEntity.setActive(Boolean.FALSE);

        this.userRepository.saveAndFlush(userEntity);
    }

    public void promoteModerator (Long userId, CurrentUser requester) {

        ensureAdmin(requester);

        final UserEntity userEntity = this.findById(userId);

        if (hasRole(userEntity, Role.MODERATOR)) {
            throw new IllegalStateException("Trying to promote an user who already has role: moderator!");
        }

        final RoleEntity roleEntity = getRole(Role.MODERATOR);
        userEntity.addRoles(roleEntity);

        this.userRepository.saveAndFlush(userEntity);
    }

    public void demoteModerator (Long userId, CurrentUser requester) {

        ensureAdmin(requester);

        final UserEntity userEntity = this.findById(userId);

        if (!hasRole(userEntity, Role.MODERATOR)) {
            throw new IllegalStateException("Trying to demote an user who does not have role: moderator!");
        }

        final RoleEntity roleEntity = getRole(Role.MODERATOR);
        userEntity.removeRoles(roleEntity);

        this.userRepository.saveAndFlush(userEntity);
    }

    private static void ensureAdminOrModerator (CurrentUser requester) {

        if (!requester.isAdmin() && !requester.isModerator()) {
            throw new NotAuthorizedException("What are you trying to do?");
        }
    }

    private static void ensureAdmin (CurrentUser requester) {

        if (!requester.isAdmin()) {
            throw new NotAuthorizedException("What are you trying to do?");
        }
    }

    private static boolean hasRole (UserEntity userEntity, Role role) {

        return userEntity.getRoles().stream().anyMatch(r -> r.getRole().equals(role));
    }

    private RoleEntity getRole (Role role) {

        return this.roleRepository.getByRole(role);
    }
}
