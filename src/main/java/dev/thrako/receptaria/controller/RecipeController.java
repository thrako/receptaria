package dev.thrako.receptaria.controller;

import dev.thrako.receptaria.model.photo.dto.SavedPhotoDTO;
import dev.thrako.receptaria.model.recipe.dto.RecipeBM;
import dev.thrako.receptaria.model.recipe.dto.RecipeShortDTO;
import dev.thrako.receptaria.repository.TempPhotoRepository;
import dev.thrako.receptaria.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static dev.thrako.receptaria.constant.Constants.BINDING_RESULT_PATH;

@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private final UnitService unitService;
    private final TempPhotoRepository tempPhotoRepository;

    @Autowired
    public RecipeController (RecipeService recipeService,
                             UnitService unitService,
                             TempPhotoRepository tempPhotoRepository) {

        this.recipeService = recipeService;
        this.unitService = unitService;
        this.tempPhotoRepository = tempPhotoRepository;
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
    public String addRecipe (Model model) {

        final RecipeBM recipeBM = (RecipeBM) model.getAttribute("recipeBM");
        if (null != recipeBM &&
                null == recipeBM.getTempRecipeId()) {

            recipeBM.setTempRecipeId(UUID.randomUUID());
        }


        return "recipes/add";
    }

    @PostMapping("/recipes/add")
    public String addRecipe (@Valid RecipeBM recipeBM,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttrs,
                             @AuthenticationPrincipal UserDetails principal) {

        recipeBM.setSavedPhotoDTOS(tempPhotoRepository.findAllByRecipeBMId(recipeBM.getTempRecipeId())
                .stream()
                .map(SavedPhotoDTO::fromTempEntity)
                .map(dto -> dto.setPrimary(recipeBM.getPrimaryPhotoId().equals(dto.getId())))
                .toList());

        if (bindingResult.hasErrors()) {
            redirectAttrs.addFlashAttribute("recipeBM", recipeBM);
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.recipeBM", bindingResult);
            return "redirect:/recipes/add";
        }


        if (this.recipeService.save(recipeBM, principal.getUsername())) {
            return "redirect:/recipes/add-success";
        }

        throw new RuntimeException();
    }

    @GetMapping("/recipes/add-success")
    public String recipeSuccess () {

        return "recipes/add-success";
    }

    @GetMapping("/recipes/all")
    public String getAll (Model model) {

        final List<RecipeShortDTO> recipeCards = this.recipeService.getRecipeCards();
        model.addAttribute("recipeCards", recipeCards);

        return "recipes/all";
    }

}
