package dev.thrako.receptaria.service.init;

import dev.thrako.receptaria.common.constant.Role;
import dev.thrako.receptaria.common.constant.VisibilityStatus;
import dev.thrako.receptaria.model.entity.role.RoleEntity;
import dev.thrako.receptaria.model.entity.unit.UnitEntity;
import dev.thrako.receptaria.model.entity.user.UserEntity;
import dev.thrako.receptaria.model.entity.visibility.VisibilityEntity;
import dev.thrako.receptaria.model.repository.RoleRepository;
import dev.thrako.receptaria.model.repository.UnitRepository;
import dev.thrako.receptaria.model.repository.UserRepository;
import dev.thrako.receptaria.model.repository.VisibilityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
public class InitDataService implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final VisibilityRepository visibilityRepository;
    private final PasswordEncoder passwordEncoder;

    public InitDataService (RoleRepository roleRepository,
                            UserRepository userRepository,
                            UnitRepository unitRepository,
                            VisibilityRepository visibilityRepository, PasswordEncoder passwordEncoder) {

        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.visibilityRepository = visibilityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run (String... args) {

        if (this.roleRepository.findAll().isEmpty()) initRoles();
        if (this.userRepository.findAll().isEmpty()) initUsers();
        if (this.unitRepository.findAll().isEmpty()) initUnits();
        if (this.visibilityRepository.findAll().isEmpty()) initVisibilityStatuses();
    }

    public void initRoles () {

        final List<RoleEntity> roleEntities = Arrays.stream(Role.values())
                .map(RoleEntity::new)
                .toList();
        this.roleRepository.saveAllAndFlush(roleEntities);
    }

    private void initUsers () {

        var roleUser = roleRepository.getByRole(Role.USER);
        var roleModerator = roleRepository.getByRole(Role.MODERATOR);
        var roleAdmin = roleRepository.getByRole(Role.ADMIN);
        java.util.List<UserEntity> users = new ArrayList<>();

        users.add(new UserEntity()
                .setEmail("alibaba@gmail.com")
                .setPassword(this.passwordEncoder.encode("1234"))
                .setFirstName("Aladin")
                .setLastName("Babaitov")
                .setDisplayName("Ali Baba")
                .setActive(true)
                .addRoles(roleUser)
        );

        users.add(new UserEntity()
                .setEmail("khanko@brat.bg")
                .setPassword(this.passwordEncoder.encode("1234"))
                .setFirstName("Khan")
                .setLastName("Kubrat")
                .setDisplayName("Khanko Brat")
                .setActive(true)
                .addRoles(roleUser, roleModerator, roleAdmin)
        );

        users.add(new UserEntity()
                .setEmail("tor@balan.com")
                .setPassword(this.passwordEncoder.encode("1234"))
                .setFirstName("Tor")
                .setLastName("Balan")
                .setDisplayName("Torbalan")
                .setActive(true)
                .addRoles(roleUser, roleModerator)
        );

        this.userRepository.saveAllAndFlush(users);
    }

    private void initUnits () {

        this.unitRepository.saveAllAndFlush(Stream.of(
                        "tbsp",
                        "tsp",
                        "mL",
                        "L",
                        "g",
                        "kg",
                        "drop",
                        "pinch",
                        "to taste"
                )
                .map(UnitEntity::new)
                .toList());
    }

    private void initVisibilityStatuses () {
        final List<VisibilityEntity> visibilityEntities = Arrays.stream(VisibilityStatus.values())
                .map(VisibilityEntity::new)
                .toList();
        this.visibilityRepository.saveAllAndFlush(visibilityEntities);
    }

}
