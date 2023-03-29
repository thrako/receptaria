package dev.thrako.receptaria.model.user;

import dev.thrako.receptaria.model.recipe.RecipeEntity;
import dev.thrako.receptaria.model.role.RoleEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static dev.thrako.receptaria.constant.Constants.ROLE_PREFIX;

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
        private Set<RecipeEntity> likedRecipes;

        public UserEntity () {

                this.roles = new ArrayList<>();
                this.likedRecipes = new HashSet<>();
        }

        public UserEntity addRoles (RoleEntity... roles) {

                this.roles.addAll(List.of(roles));
                return this;
        }

        public boolean removeRoles (RoleEntity... roles) {

                return this.roles.removeAll(List.of(roles));
        }

        public void likeRecipe (RecipeEntity recipe) {

                this.likedRecipes.add(recipe);
        }

        public boolean unlikeRecipe (RecipeEntity recipe) {

                return this.likedRecipes.remove(recipe);
        }

}
