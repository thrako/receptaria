package dev.thrako.receptaria.web.controller;

import dev.thrako.receptaria.model.entity.recipe.dto.RecipeBM;
import dev.thrako.receptaria.model.entity.recipe.dto.RecipeCardVM;
import dev.thrako.receptaria.model.security.CurrentUser;
import dev.thrako.receptaria.service.utility.RecipeKeeper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static dev.thrako.receptaria.common.constant.Constants.BINDING_RESULT_PATH;

@Controller
public class RecipeController {

    private final RecipeKeeper recipeKeeper;


    @Autowired
    public RecipeController (RecipeKeeper recipeKeeper) {

        this.recipeKeeper = recipeKeeper;
    }


    @ModelAttribute("units")
    public void initUnits (Model model) {

        model.addAttribute("units", recipeKeeper.getDistinctUnitNames());
    }

    @ModelAttribute("recipeBM")
    public void initRecipeBM (Model model) {

        model.addAttribute("recipeBM", new RecipeBM());
    }

    @GetMapping("/recipes/add")
    public String addRecipe (Model model,
                             @AuthenticationPrincipal CurrentUser author) {

        final RecipeBM recipeBM = (RecipeBM) model.getAttribute("recipeBM");
        if (null != recipeBM &&
            null == recipeBM.getTempRecipeId()) {

            recipeBM
                    .setTempRecipeId(UUID.randomUUID())
                    .setAuthorId(author.getId());
        }

        return "recipes/add";
    }

    @PostMapping("/recipes/add")
    public String addRecipe (@Valid RecipeBM recipeBM,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttrs,
                             @AuthenticationPrincipal CurrentUser author) {

        this.recipeKeeper.process(recipeBM);

        checkUniqueTitle(bindingResult);

        if (bindingResult.hasErrors()) {

            redirectAttrs.addFlashAttribute("recipeBM", recipeBM);
            redirectAttrs.addFlashAttribute(BINDING_RESULT_PATH.concat("recipeBM"), bindingResult);

            return "redirect:/recipes/add";
        }

        final Long recipeId = this.recipeKeeper.save(recipeBM, author.getId());

        return "redirect:/recipes/%d".formatted(recipeId);
    }

    @GetMapping("/recipes/{id}")
    public String getRecipe (@PathVariable Long id,
                             Model model,
                             @AuthenticationPrincipal CurrentUser currentUser) {

        model.addAttribute("recipeVM", this.recipeKeeper.getRecipeVMForUser(id, currentUser));
        model.addAttribute("contextAuthorities", currentUser.getContextAuthorities());
        model.addAttribute("contextRoles", currentUser.getContextRoles());

        return "recipes/show";
    }

    @GetMapping("/recipes/all")
    public String getAll (Model model, @AuthenticationPrincipal CurrentUser currentUser) {

        final List<RecipeCardVM> recipeCards = this.recipeKeeper.getCardsAll(currentUser);
        model.addAttribute("recipeCards", recipeCards);

        return "recipes/list";
    }

    @GetMapping("/recipes/mine")
    public String getOwn (Model model, @AuthenticationPrincipal CurrentUser currentUser) {

        final List<RecipeCardVM> recipeCards = this.recipeKeeper.getCardsOwn(currentUser);
        model.addAttribute("recipeCards", recipeCards);

        return "recipes/list";
    }

    @GetMapping("/recipes/author/{authorId}")
    public String getOwn (Model model,
                          @AuthenticationPrincipal CurrentUser currentUser,
                          @PathVariable Long authorId) {

        final List<RecipeCardVM> recipeCards = this.recipeKeeper.getCardsByAuthor(currentUser, authorId);
        model.addAttribute("recipeCards", recipeCards);

        return "recipes/list";
    }

    private static void checkUniqueTitle (BindingResult bindingResult) {

        if (bindingResult.getGlobalErrors()
                .stream()
                .anyMatch(error -> Objects.requireNonNull(error.getCode()).contains("UniqueRecipeForUser"))) {

            bindingResult.addError(new FieldError("recipeBM",
                    "title",
                    "You already have recipe with the same title."));
        }
    }

}
