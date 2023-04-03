package dev.thrako.receptaria.model.entity.user;

import dev.thrako.receptaria.model.entity.recipe.RecipeEntity;
import dev.thrako.receptaria.model.entity.role.RoleEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String displayName;

    @Column(unique = true)
    @Nonnull
    private String email;

    @Column
    @Nonnull
    private String password;

    @Column
    private boolean active;

    @ManyToMany
    private List<RoleEntity> roles;

    @ManyToMany
    @JoinTable(name = "recipes_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private List<RecipeEntity> likedRecipes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "follow_relations",
            joinColumns = @JoinColumn(name = "follower"),
            inverseJoinColumns = @JoinColumn(name = "followed")
    )
    private List<UserEntity> followers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "block_list",
            joinColumns = @JoinColumn(name = "blocker"),
            inverseJoinColumns = @JoinColumn(name = "blocked")
    )
    private List<UserEntity> blockList;

    public UserEntity () {

        this.roles = new ArrayList<>();
        this.likedRecipes = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.blockList = new ArrayList<>();
    }

    public UserEntity addRoles (RoleEntity... roles) {

        this.roles.addAll(List.of(roles));
        return this;
    }

    public boolean removeRoles (RoleEntity... roles) {

        return this.roles.removeAll(List.of(roles));
    }

    public boolean likeRecipe (RecipeEntity recipe) {

        return this.likedRecipes.add(recipe);
    }

    public boolean unlikeRecipe (RecipeEntity recipe) {

        return this.likedRecipes.remove(recipe);
    }
    public boolean isFollowedBy (UserEntity candidate) {

        return this.followers.contains(candidate);
    }

    public Boolean hasBlocked (UserEntity candidate) {

        return this.blockList.contains(candidate);
    }
}
