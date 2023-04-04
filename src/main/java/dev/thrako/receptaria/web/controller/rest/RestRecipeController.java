package dev.thrako.receptaria.web.controller.rest;

import dev.thrako.receptaria.common.message.Message;
import dev.thrako.receptaria.model.security.CurrentUser;
import dev.thrako.receptaria.service.utility.RecipeKeeper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestRecipeController {

    private final RecipeKeeper recipeKeeper;

    public RestRecipeController (RecipeKeeper recipeKeeper) {

        this.recipeKeeper = recipeKeeper;
    }

    @GetMapping("/api/recipes/isAvailable/{title}")
    public Boolean recipeExists (@PathVariable String title,
                                 @AuthenticationPrincipal CurrentUser author) {

        return recipeKeeper.isAvailableRecipeTitleForAuthor(title, author);
    }

    @PostMapping("api/recipes/{id}/like")
    public ResponseEntity<Message> like (@PathVariable Long id,
                                         @AuthenticationPrincipal CurrentUser visitor) {

        final int likesCount = this.recipeKeeper.addLike(id, visitor);
        return ResponseEntity.status(HttpStatus.OK).body(Message.from(likesCount));
    }

    @PostMapping("api/recipes/{id}/unlike")
    public ResponseEntity<Message> unlike (@PathVariable Long id,
                                  @AuthenticationPrincipal CurrentUser visitor) {

        final int likesCount = this.recipeKeeper.removeLike(id, visitor);
        return ResponseEntity.status(HttpStatus.OK).body(Message.from(likesCount));
    }

}
