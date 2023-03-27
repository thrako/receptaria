package dev.thrako.receptaria.model.photo;

import dev.thrako.receptaria.model.photo.dto.PhotoBM;
import dev.thrako.receptaria.model.recipe.RecipeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "photos")
public class PhotoEntity {

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
    private Boolean isPrimary;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private RecipeEntity recipe;

}
