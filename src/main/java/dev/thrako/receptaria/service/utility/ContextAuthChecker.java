package dev.thrako.receptaria.service.utility;

import dev.thrako.receptaria.common.constant.ContextAuthority;
import dev.thrako.receptaria.common.constant.ContextRole;
import dev.thrako.receptaria.model.entity.user.UserEntity;
import dev.thrako.receptaria.common.security.CurrentUser;
import dev.thrako.receptaria.service.RecipeService;
import dev.thrako.receptaria.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class ContextAuthChecker {

    private final RecipeService recipeService;
    private final UserService userService;

    public ContextAuthChecker (RecipeService recipeService, UserService userService) {

        this.recipeService = recipeService;
        this.userService = userService;
    }

    public Boolean check (ContextAuthority authority, CurrentUser currentUser, long recipeId) {

        switch (authority) {
            case VIEW -> {
                return this.recipeService.checkCanView(currentUser, recipeId);
            }
            case EDIT -> {
                return this.recipeService.checkCanEdit(currentUser, recipeId);
            }
            case DELETE -> {
                return this.recipeService.checkCanDelete(currentUser, recipeId);
            }
            case LIKE -> {
                return ! this.recipeService.isAuthor(currentUser, recipeId);
            }

            default -> throw new IllegalStateException("Unexpected value: " + authority);
        }

    }

    public Boolean check (ContextRole contextRole, CurrentUser currentUser, long recipeId) {

        final UserEntity author = this.recipeService.findById(recipeId).getAuthor();

        switch (contextRole) {
            case AUTHOR -> {
                return author.getId().equals(currentUser.getId());
            }
            case FOLLOWER -> {

                return author.isFollowedBy(this.userService.findById(currentUser.getId()));
            }
            case BLOCKED -> {

                return author.hasBlocked(this.userService.findById(currentUser.getId()));
            }

            default -> throw new IllegalStateException("Unexpected value: " + contextRole);
        }
    }
}
