package dev.thrako.receptaria.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithUserDetails(value = "alibaba@gmail.com")
    void getAddRecipe_byLoggedUser_returnsProperView () throws Exception {

        mockMvc.perform(get("/recipes/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("units"))
                .andExpect(model().attributeExists("recipeBM"))
                .andExpect(view().name("recipes/add"));
    }

    @Test
    void testAddRecipe () {

    }

    @Test
    void getRecipe () {

    }

    @Test
    void getAll () {

    }

    @Test
    void getOwn () {

    }

    @Test
    void testGetOwn () {

    }
}