package dev.thrako.receptaria.repository;

import dev.thrako.receptaria.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserById (Long id);

    UserEntity getUserEntityById (Long id);

    Optional<UserEntity> findUserByEmail (String email);

    boolean existsByEmail (String email);
}
