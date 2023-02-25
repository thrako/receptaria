package dev.thrako.receptaria.repository;

import dev.thrako.receptaria.model.unit.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<UnitEntity, Long> {

    Optional<UnitEntity> findUnitByName (String unitName);

    @Query("select distinct u.name from UnitEntity u")
    List<String> findAllDistinctNames ();
}
