package dev.thrako.receptaria.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thrako.receptaria.model.entity.recipe.dto.RecipeBM;
import dev.thrako.receptaria.model.entity.recipe.dto.RecipeCardVM;
import dev.thrako.receptaria.model.security.CurrentUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.FlashMap;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithAnonymousUser
    void getAddRecipe_byAnonymous_redirectsToLogin () throws Exception {

        mockMvc.perform(get("/recipes/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithUserDetails(value = "alibaba@gmail.com")
    void getAddRecipe_byLoggedUser_returnsProperView () throws Exception {

        mockMvc.perform(get("/recipes/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("units"))
                .andExpect(model().attributeExists("recipeBM"))
                .andExpect(view().name("recipes/add"));
    }

    @Test
    @WithAnonymousUser
    void postAddRecipe_byAnonymousUser_redirectsToLogin () throws Exception {

        mockMvc.perform(post("/recipes/add")
                                .param("title", "testTitle")
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithUserDetails(
            value = "alibaba@gmail.com",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void postAddRecipe_byLoggedUserWithPartialData_redirectsWithErrorMessages () throws Exception {

        RecipeBM recipeBM = new RecipeBM()
                .setAuthorId(1L)
                .setTempRecipeId(UUID.randomUUID());

        final FlashMap flashMap
                = mockMvc.perform(post("/recipes/add")
                                          .param("title", "testTitle")
                                          .flashAttr("recipeBM", recipeBM)
                                          .with(csrf())
                )
                .andExpect(status().isBadRequest())
                .andExpect(redirectedUrl("/recipes/add"))
                .andReturn().getFlashMap();

        assertTrue(flashMap.containsKey("recipeBM"));
        assertTrue(flashMap.containsKey("org.springframework.validation.BindingResult.recipeBM"));
        BindingResult bindingResult = (BindingResult) flashMap.get(
                "org.springframework.validation.BindingResult.recipeBM");
        assertTrue(bindingResult.hasErrors());
        assertTrue(bindingResult.hasFieldErrors("servings"));
        assertTrue(bindingResult.hasFieldErrors("description"));
        assertTrue(bindingResult.hasFieldErrors("visibilityStatus"));
    }

    @Test
    @WithUserDetails(
            value = "alibaba@gmail.com",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void postAddRecipe_byLoggedUserWithCompleteData_returnsRedirectToNewlyCreated () throws Exception {

        RecipeBM recipeBM = new RecipeBM()
                .setAuthorId(1L)
                .setTempRecipeId(UUID.randomUUID());

        mockMvc.perform(post("/recipes/add")
                                .param("title", "testShopskaSalad")
                                .param("preparationHours", "1")
                                .param("preparationMinutes", "15")
                                .param("cookingHours", "0")
                                .param("cookingMinutes", "50")
                                .param("servings", "6")
                                .param("listIngredientBM[0].ingredientName", "cucumbers")
                                .param("listIngredientBM[0].quantity", "200")
                                .param("listIngredientBM[0].unitName", "gr")
                                .param("listIngredientBM[1].ingredientName", "tomatoes")
                                .param("listIngredientBM[1].quantity", "2")
                                .param("listIngredientBM[1].unitName", "pcs")
                                .param("listIngredientBM[2].ingredientName", "white cheese")
                                .param("listIngredientBM[2].quantity", "")
                                .param("listIngredientBM[2].unitName", "to taste")
                                .param("description",
                                       "Chop all the vegetables. Grate some cheese. Add hot paper if you like. Goes very well with rakiya.")
                                .param("visibilityStatus", "PUBLIC")
                                .flashAttr("recipeBM", recipeBM)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(0))
                .andExpect(redirectedUrlPattern("/recipes/{[0-9]*}"));
    }

    @Test
    @WithUserDetails(
            value = "alibaba@gmail.com",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void fullCycleAddRecipe_byLoggedUserWithCompleteDataAndPhotos_worksAsExpected () throws Exception {

        //noinspection ConstantConditions
        final RecipeBM recipeBM
                = (RecipeBM) mockMvc.perform(get("/recipes/add"))
                .andExpect(model().attributeExists("recipeBM"))
                .andReturn().getModelAndView().getModel().get("recipeBM");

        File imageFile = new File("src/test/resources", "testImgOne.png");

        MockMultipartFile imageMultipartFile
                = new MockMultipartFile(
                "fileData",
                imageFile.getName(),
                "image/png",
                Files.readAllBytes(imageFile.toPath()));

        final var imageUploadString
                = mockMvc.perform(multipart("/api/photos/temp/upload")
                                          .part(new MockPart("fileData",
                                                             imageFile.getName(),
                                                             imageMultipartFile.getBytes()))
                                          .contentType(
                                                  "multipart/form-data; boundary=----WebKitFormBoundaryk1fn4D7ebCjmFPHl")
                                          .param("description", "testDescription")
                                          .param("tempRecipeId",
                                                 recipeBM.getTempRecipeId().toString())
                                          .with(csrf())

                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(nullValue())))
                .andExpect(jsonPath("$.tempId", not(notANumber())))
                .andExpect(jsonPath("$.tempRecipeId", notNullValue()))
                .andReturn().getResponse().getContentAsString();

        //noinspection unchecked
        Map<String, Object> imageUploadMapOne = new ObjectMapper().readValue(imageUploadString, HashMap.class);
        final String uploadPhotoOneTempId = imageUploadMapOne.get("tempId").toString();
        final String uploadPhotoOneTempRecipeId = imageUploadMapOne.get("tempRecipeId").toString();

        final String redirectedUrl
                = mockMvc.perform(post("/recipes/add")
                                          .param("title", "testShopskaSaladFull")
                                          .param("recipeBM.photoVMList[0].tempId}",
                                                 uploadPhotoOneTempId)
                                          .param("recipeBM.photoVMList[0].tempRecipeId}",
                                                 uploadPhotoOneTempRecipeId)
                                          .param("preparationHours", "1")
                                          .param("preparationMinutes", "15")
                                          .param("cookingHours", "0")
                                          .param("cookingMinutes", "50")
                                          .param("servings", "6")
                                          .param("listIngredientBM[0].ingredientName", "cucumbers")
                                          .param("listIngredientBM[0].quantity", "200")
                                          .param("listIngredientBM[0].unitName", "gr")
                                          .param("listIngredientBM[1].ingredientName", "tomatoes")
                                          .param("listIngredientBM[1].quantity", "2")
                                          .param("listIngredientBM[1].unitName", "pcs")
                                          .param("listIngredientBM[2].ingredientName",
                                                 "white cheese")
                                          .param("listIngredientBM[2].quantity", "")
                                          .param("listIngredientBM[2].unitName", "to taste")
                                          .param("description",
                                                 "Chop all the vegetables. Grate some cheese. Add hot paper if you like. Goes very well with rakiya.")
                                          .param("visibilityStatus", "PUBLIC")
                                          .flashAttr("recipeBM", recipeBM)
                                          .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(0))
                .andExpect(redirectedUrlPattern("/recipes/{[0-9]*}"))
                .andReturn().getResponse().getRedirectedUrl();

        //noinspection ConstantConditions
        mockMvc.perform(get(redirectedUrl))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/show"))
                .andExpect(model().attributeExists("recipeVM"))
                .andExpect(model().attributeExists("contextAuthorities"))
                .andExpect(model().attributeExists("contextRoles"));

        final FlashMap flashMap
                = mockMvc.perform(post("/recipes/add")
                                          .param("title", "testShopskaSaladFull")
                                          .param("preparationHours", "1")
                                          .param("preparationMinutes", "15")
                                          .param("cookingHours", "0")
                                          .param("cookingMinutes", "50")
                                          .param("servings", "6")
                                          .param("listIngredientBM[0].ingredientName", "cucumbers")
                                          .param("listIngredientBM[0].quantity", "200")
                                          .param("listIngredientBM[0].unitName", "gr")
                                          .param("listIngredientBM[1].ingredientName", "tomatoes")
                                          .param("listIngredientBM[1].quantity", "2")
                                          .param("listIngredientBM[1].unitName", "pcs")
                                          .param("listIngredientBM[2].ingredientName",
                                                 "white cheese")
                                          .param("listIngredientBM[2].quantity", "")
                                          .param("listIngredientBM[2].unitName", "to taste")
                                          .param("description",
                                                 "Chop all the vegetables. Grate some cheese. Add hot paper if you like. Goes very well with rakiya.")
                                          .param("visibilityStatus", "PUBLIC")
                                          .flashAttr("recipeBM", recipeBM)
                                          .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(redirectedUrl("/recipes/add"))
                .andReturn().getFlashMap();

        assertTrue(flashMap.containsKey("recipeBM"));
        assertTrue(flashMap.containsKey("org.springframework.validation.BindingResult.recipeBM"));
        BindingResult bindingResult = (BindingResult) flashMap.get(
                "org.springframework.validation.BindingResult.recipeBM");
        assertTrue(bindingResult.hasErrors());
        assertTrue(bindingResult.hasFieldErrors("title"));
    }

    @Test
    @WithAnonymousUser
    void getAll_byAnonymousUser_returnsOK () throws Exception {

        mockMvc.perform(get("/recipes/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/list"))
                .andExpect(model().attributeExists("recipeCards"));
    }

    @Test
    @WithUserDetails(
            value = "alibaba@gmail.com",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void getAll_byLoggedUser_returnsOK () throws Exception {

        mockMvc.perform(get("/recipes/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/list"))
                .andExpect(model().attributeExists("recipeCards"));
    }


    @Test
    @WithAnonymousUser
    void getOwn_byAnonymous_redirectsToLogin () throws Exception {

        mockMvc.perform(get("/recipes/mine"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithUserDetails(
            value = "alibaba@gmail.com",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void getOwn_byLoggedUser_returnsOk () throws Exception {

        final CurrentUser currentUser
                = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final Long currentUserIdString = currentUser.getId();

        // noinspection ConstantConditions,unchecked
        List<RecipeCardVM> recipeCardVMList
                = (List<RecipeCardVM>) mockMvc.perform(get("/recipes/mine"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("recipes/list"))
                    .andExpect(model().attributeExists("recipeCards"))
                    .andReturn().getModelAndView().getModel().get("recipeCards");

        assertTrue(recipeCardVMList.stream()
                           .filter(rc -> ! rc.getAuthorId().equals(currentUserIdString))
                           .findAny()
                           .isEmpty());
    }

    @Test
    @WithAnonymousUser
    void getByAuthor_byAnonymous_redirectsToLogin() throws Exception {

        mockMvc.perform(get("/recipes/author/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithUserDetails(
            value = "alibaba@gmail.com",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void getByAuthor_byLoggedUser_returnsOk() throws Exception {

        Long authorId = 2L;

        //noinspection unchecked,ConstantConditions
        List<RecipeCardVM> recipeCardVMList
                = (List<RecipeCardVM>) mockMvc.perform(get("/recipes/author/%d".formatted(authorId)))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/list"))
                .andExpect(model().attributeExists("recipeCards"))
                .andReturn().getModelAndView().getModel().get("recipeCards");

        assertTrue(recipeCardVMList.stream()
                           .filter(rc -> ! rc.getAuthorId().equals(authorId))
                           .findAny()
                           .isEmpty());
    }

}