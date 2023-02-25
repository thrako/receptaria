package dev.thrako.receptaria.repository;

import dev.thrako.receptaria.model.photo.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {

}
