package dev.thrako.receptaria.model.repository;

import dev.thrako.receptaria.model.entity.photo.TempPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TempPhotoRepository extends JpaRepository<TempPhotoEntity, Long> {

    List<TempPhotoEntity> findAllByTempRecipeId (UUID tempRecipeId);
}
