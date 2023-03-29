package dev.thrako.receptaria.controller;

import dev.thrako.receptaria.model.photo.dto.PhotoVM;
import dev.thrako.receptaria.model.recipe.dto.RecipeBM;
import dev.thrako.receptaria.model.recipe.dto.RecipeCardDTO;
import dev.thrako.receptaria.security.CurrentUser;
import dev.thrako.receptaria.service.*;
import dev.thrako.receptaria.utility.RecipeKeeper;
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

import static dev.thrako.receptaria.constant.Constants.BINDING_RESULT_PATH;

@Controller
public class RecipeController {

    private final RecipeKeeper recipeKeeper;
    private final RecipeService recipeService;
    private final UnitService unitService;
    private final TempPhotoService tempPhotoService;


    @Autowired
    public RecipeController (RecipeKeeper recipeKeeper,
                             RecipeService recipeService,
                             UnitService unitService,
                             TempPhotoService tempPhotoService) {

        this.recipeKeeper = recipeKeeper;
        this.recipeService = recipeService;
        this.unitService = unitService;
        this.tempPhotoService = tempPhotoService;
    }


    @ModelAttribute("units")
    public void initUnits (Model model) {

        model.addAttribute("units", unitService.getDistinctUnitNames());
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

        final UUID tempRecipeId = recipeBM.getTempRecipeId();
        final Long primaryPhotoId = recipeBM.getPrimaryPhotoId();

        this.tempPhotoService.updatePrimaryFlag(tempRecipeId, primaryPhotoId);

        final List<PhotoVM> photoVMList = this.tempPhotoService.getListPhotoVM(tempRecipeId);
        recipeBM.setPhotoVMList(photoVMList);

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

        final RecipeCardDTO recipeCard = this.recipeService.getRecipeCard(id);

        model.addAttribute("recipeCard", recipeCard);

        return "recipes/show";
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

    @GetMapping("/recipes/add-success")
    public String recipeSuccess () {

        return "recipes/add-success";
    }

    @GetMapping("/recipes/all")
    public String getAll (Model model) {

        final List<RecipeCardDTO> recipeCards = this.recipeService.getRecipeCards();
        model.addAttribute("recipeCards", recipeCards);

        return "recipes/all";
    }

}
