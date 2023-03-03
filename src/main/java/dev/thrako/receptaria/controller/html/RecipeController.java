package dev.thrako.receptaria.controller.html;

import dev.thrako.receptaria.model.ingredient.dto.IngredientDTO;
import dev.thrako.receptaria.model.photo.dto.PhotoUploadDTO;
import dev.thrako.receptaria.model.recipe.dto.RecipeDTO;
import dev.thrako.receptaria.model.recipe.dto.RecipeShortDTO;
import dev.thrako.receptaria.service.IngredientService;
import dev.thrako.receptaria.service.PhotoService;
import dev.thrako.receptaria.service.RecipeService;
import dev.thrako.receptaria.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class RecipeController {

    private static final IngredientDTO EMPTY_INGREDIENT = new IngredientDTO("", "", "");

    private final IngredientService ingredientService;
    private final PhotoService photoService;
    private final RecipeService recipeService;
    private final UnitService unitService;

    @Autowired
    public RecipeController (IngredientService ingredientService,
                             PhotoService photoService,
                             RecipeService recipeService,
                             UnitService unitService
    ) {

        this.ingredientService = ingredientService;
        this.photoService = photoService;
        this.recipeService = recipeService;
        this.unitService = unitService;
    }

    @ModelAttribute("recipeDTO")
    public void initRecipeDTO (Model model) {

        final RecipeDTO recipeDTO = new RecipeDTO().addIngredientDTO(EMPTY_INGREDIENT);
        model.addAttribute("recipeDTO", recipeDTO);
        model.addAttribute("units", unitService.getDistinctUnitNames());
    }

    @GetMapping("/recipes/add")
    public String addRecipe () {

        return "recipes/add";
    }

    @PostMapping("/recipes/add")
    public String addRecipe (@Valid RecipeDTO recipeDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttrs,
                             MultipartHttpServletRequest multipartRequest,
                             Principal principal) {

        List<MultipartFile> multipartFiles = multipartRequest.getFiles("photo-file");
        final List<PhotoUploadDTO> photoDtoList = this.photoService.getPhotoDtoList(multipartFiles);
        recipeDTO.setPhotoDTOS(photoDtoList);



//        final Map<String, String[]> parameterMap = multipartRequest.getParameterMap();
//        List<IngredientDTO> ingredientDtoList = ingredientService.getIngredientDtoList(parameterMap);
//
//        if (ingredientDtoList.isEmpty()) {
//            ingredientDtoList = new ArrayList<>();
//            ingredientDtoList.add(EMPTY_INGREDIENT);
//            final String noIngredientsMsg = "Please add at least one ingredient!";
//            bindingResult.addError(new FieldError("recipeDTO", "ingredientDTOS", noIngredientsMsg));
//        }
//
//        recipeDTO.setIngredientDTOS(ingredientDtoList);

        if (bindingResult.hasErrors()) {
            redirectAttrs.addFlashAttribute("recipeDTO", recipeDTO);
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.recipeDTO",
                    bindingResult);
            return "redirect:/recipes/add";
        }


        if (this.recipeService.save(recipeDTO, principal.getName())) {
            return "redirect:/recipes/add-success";
        }

        return "redirect:/";
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
