package dev.thrako.receptaria.model.entity.photo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "temp_photos")
public class TempPhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String publicId;

    @Column
    private String url;

    @Column
    private String description;

    @Column
    private String filename;

    @Column
    private boolean isPrimary;

    @Column
    private UUID tempRecipeId;

    @CreationTimestamp
    private LocalDateTime addedOn;

}
