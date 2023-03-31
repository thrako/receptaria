package dev.thrako.receptaria.model.repository;

import dev.thrako.receptaria.model.entity.visibility.VisibilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisibilityRepository extends JpaRepository<VisibilityEntity, Long> {

}
