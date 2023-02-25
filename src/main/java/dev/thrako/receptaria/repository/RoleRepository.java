package dev.thrako.receptaria.repository;

import dev.thrako.receptaria.constant.Role;
import dev.thrako.receptaria.model.role.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity getByRole (Role role);
}
