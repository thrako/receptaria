package dev.thrako.receptaria.model.entity.recipe;

import dev.thrako.receptaria.constant.VisibilityStatus;
import dev.thrako.receptaria.model.entity.ingredient.IngredientEntity;
import dev.thrako.receptaria.model.entity.user.UserEntity;
import dev.thrako.receptaria.model.entity.photo.PhotoEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "recipes")
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.ALL
    )
    private List<PhotoEntity> photos;

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.ALL
    )
    private List<IngredientEntity> ingredients;

    @Column
    private Duration preparationTime;

    @Column
    private Duration cookingTime;

    @Column
    private Integer servings;

    @ManyToMany(mappedBy = "likedRecipes")
    private Set<UserEntity> likes;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private VisibilityStatus visibilityStatus;

    @ManyToOne
    private UserEntity author;

    @CreationTimestamp
    private LocalDateTime addedOn;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    public RecipeEntity () {

        this.likes = new HashSet<>();
        this.photos = new ArrayList<>();
        this.ingredients = new ArrayList<>();

        this.preparationTime = Duration.ZERO;
        this.cookingTime = Duration.ZERO;
    }

    public RecipeEntity addPreparationHours (int hours) {

        this.preparationTime = this.preparationTime.plusHours(hours);

        return this;
    }

    public RecipeEntity addPreparationMinutes (int minutes) {

        this.preparationTime = this.preparationTime.plusMinutes(minutes);

        return this;
    }

    public RecipeEntity resetPreparationTime () {

        this.preparationTime = Duration.ZERO;

        return this;
    }

    public RecipeEntity addCookingHours (int hours) {

        this.cookingTime = this.cookingTime.plusHours(hours);

        return this;
    }

    public RecipeEntity addCookingMinutes (int minutes) {

        this.cookingTime = this.cookingTime.plusMinutes(minutes);

        return this;
    }

    public RecipeEntity resetCookingTime () {

        this.cookingTime = Duration.ZERO;

        return this;
    }

    public RecipeEntity addPhotos (List<PhotoEntity> photos) {

        this.photos.addAll(photos);
        return this;
    }

    public RecipeEntity addProducts (List<IngredientEntity> products) {

        this.ingredients.addAll(products);
        return this;
    }

}
