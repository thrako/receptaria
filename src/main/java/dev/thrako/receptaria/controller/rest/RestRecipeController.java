package dev.thrako.receptaria.controller.rest;

import dev.thrako.receptaria.security.CurrentUser;
import dev.thrako.receptaria.service.RecipeService;
import dev.thrako.receptaria.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class RestRecipeController {

    private final RecipeService recipeService;
    private final UserService userService;

    public RestRecipeController (RecipeService recipeService,
                                 UserService userService) {

        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping("/api/recipes/isAvailable/{title}")
    public Boolean recipeExists (@PathVariable String title,
                                 @AuthenticationPrincipal CurrentUser author) {

        return recipeService.isAvailableRecipeTitle(author.getId(), title);
    }

}
