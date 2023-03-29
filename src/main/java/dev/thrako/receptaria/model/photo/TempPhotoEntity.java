package dev.thrako.receptaria.model.photo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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

}
