package dev.thrako.receptaria.service;

import dev.thrako.receptaria.constant.Role;
import dev.thrako.receptaria.model.role.RoleEntity;
import dev.thrako.receptaria.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService (RoleRepository roleRepository) {

        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles () {

        final List<RoleEntity> roleEntities = Arrays.stream(Role.values())
                .map(RoleEntity::new)
                .toList();
        this.roleRepository.saveAllAndFlush(roleEntities);
    }
}
