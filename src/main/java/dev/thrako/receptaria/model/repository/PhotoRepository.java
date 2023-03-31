package dev.thrako.receptaria.model.repository;

import dev.thrako.receptaria.model.entity.photo.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {

}
