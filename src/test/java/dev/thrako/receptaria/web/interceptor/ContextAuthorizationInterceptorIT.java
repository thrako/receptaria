package dev.thrako.receptaria.web.interceptor;

import dev.thrako.receptaria.common.constant.VisibilityStatus;
import dev.thrako.receptaria.model.entity.recipe.RecipeEntity;
import dev.thrako.receptaria.model.entity.recipe.dto.RecipeBM;
import dev.thrako.receptaria.model.entity.recipe.dto.RecipeCardVM;
import dev.thrako.receptaria.model.entity.user.UserEntity;
import dev.thrako.receptaria.model.repository.RecipeRepository;
import dev.thrako.receptaria.model.repository.UserRepository;
import dev.thrako.receptaria.model.security.CurrentUser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContextAuthorizationInterceptorIT {

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void clearAllRecipes () throws Exception {

        this.recipeRepository.deleteAll();

        //noinspection unchecked,ConstantConditions
        final List<RecipeCardVM> recipeCards
                = (List<RecipeCardVM>) mockMvc.perform(get("/recipes/all"))
                .andExpect(model().attributeExists("recipeCards"))
                .andReturn().getModelAndView().getModel().get("recipeCards");

        assertTrue(recipeCards.isEmpty());
    }

    @Test
    @Order(2)
    @WithUserDetails(
            value = "alibaba@gmail.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    public void postSomeRecipesOne () throws Exception {

        mockMvc.perform(post("/recipes/add")
                                .flashAttr("recipeBM", new RecipeBM()
                                        .setTitle("private_1")
                                        .setServings(1)
                                        .setDescription("testDescription_1")
                                        .setVisibilityStatus(VisibilityStatus.PRIVATE)
                                        .setAuthorId(1L)
                                        .setTempRecipeId(UUID.randomUUID()))
                                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(flash().attributeCount(0))
                .andExpect(redirectedUrlPattern("/recipes/{[0-9]*}"));

        mockMvc.perform(post("/recipes/add")
                                .flashAttr("recipeBM", new RecipeBM()
                                        .setAuthorId(1L)
                                        .setTempRecipeId(UUID.randomUUID())
                                        .setTitle("followers_2")
                                        .setServings(2)
                                        .setDescription("testDescription_2")
                                        .setVisibilityStatus(VisibilityStatus.FOLLOWERS))
                                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(flash().attributeCount(0))
                .andExpect(redirectedUrlPattern("/recipes/{[0-9]*}"));

        mockMvc.perform(post("/recipes/add")
                                .flashAttr("recipeBM", new RecipeBM()
                                        .setAuthorId(3L)
                                        .setTempRecipeId(UUID.randomUUID())
                                        .setTitle("public_3")
                                        .setServings(3)
                                        .setDescription("testDescription_3")
                                        .setVisibilityStatus(VisibilityStatus.PUBLIC))
                                .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(flash().attributeCount(0))
                .andExpect(redirectedUrlPattern("/recipes/{[0-9]*}"));
    }

    @Test
    @Order(3)
    @WithUserDetails(
            value = "khanko@brat.bg",
            userDetailsServiceBeanName = "userDetailsService"
    )
    public void postSomeRecipesTwo () throws Exception {

        mockMvc.perform(post("/recipes/add")
                                .flashAttr("recipeBM", new RecipeBM()
                                        .setAuthorId(2L)
                                        .setTempRecipeId(UUID.randomUUID())
                                        .setTitle("private_4")
                                        .setServings(4)
                                        .setDescription("testDescription_4")
                                        .setVisibilityStatus(VisibilityStatus.PRIVATE))
                                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(flash().attributeCount(0))
                .andExpect(redirectedUrlPattern("/recipes/{[0-9]*}"));

        mockMvc.perform(post("/recipes/add")
                                .flashAttr("recipeBM", new RecipeBM()
                                        .setAuthorId(2L)
                                        .setTempRecipeId(UUID.randomUUID())
                                        .setTitle("followers_5")
                                        .setServings(5)
                                        .setDescription("testDescription_5")
                                        .setVisibilityStatus(VisibilityStatus.FOLLOWERS))
                                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(flash().attributeCount(0))
                .andExpect(redirectedUrlPattern("/recipes/{[0-9]*}"));

        mockMvc.perform(post("/recipes/add")
                                .flashAttr("recipeBM", new RecipeBM()
                                        .setAuthorId(2L)
                                        .setTempRecipeId(UUID.randomUUID())
                                        .setTitle("public_6")
                                        .setServings(6)
                                        .setDescription("testDescription_6")
                                        .setVisibilityStatus(VisibilityStatus.PUBLIC))
                                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(flash().attributeCount(0))
                .andExpect(redirectedUrlPattern("/recipes/{[0-9]*}"));
    }

    @Test
    @Order(4)
    @WithUserDetails(
            value = "tor@balan.com",
            userDetailsServiceBeanName = "userDetailsService"
    )
    public void postSomeRecipesThree () throws Exception {

        mockMvc.perform(post("/recipes/add")
                                .flashAttr("recipeBM", new RecipeBM()
                                        .setAuthorId(3L)
                                        .setTempRecipeId(UUID.randomUUID())
                                        .setTitle("private_7")
                                        .setServings(7)
                                        .setDescription("testDescription_7")
                                        .setVisibilityStatus(VisibilityStatus.PRIVATE))
                                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(flash().attributeCount(0))
                .andExpect(redirectedUrlPattern("/recipes/{[0-9]*}"));

        mockMvc.perform(post("/recipes/add")
                                .flashAttr("recipeBM", new RecipeBM()
                                        .setAuthorId(3L)
                                        .setTempRecipeId(UUID.randomUUID())
                                        .setTitle("followers_8")
                                        .setServings(8)
                                        .setDescription("testDescription_8")
                                        .setVisibilityStatus(VisibilityStatus.FOLLOWERS))
                                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(flash().attributeCount(0))
                .andExpect(redirectedUrlPattern("/recipes/{[0-9]*}"));

        mockMvc.perform(post("/recipes/add")
                                .flashAttr("recipeBM", new RecipeBM()
                                        .setAuthorId(3L)
                                        .setTempRecipeId(UUID.randomUUID())
                                        .setTitle("public_9")
                                        .setServings(9)
                                        .setDescription("testDescription_9")
                                        .setVisibilityStatus(VisibilityStatus.PUBLIC))
                                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(flash().attributeCount(0))
                .andExpect(redirectedUrlPattern("/recipes/{[0-9]*}"));
    }

    @Nested
    class thenCheck {

        @Test
        @WithAnonymousUser
        void getAll_byAnonymous_returnsOnlyPublic () throws Exception {

            mockMvc.perform(get("/recipes/all"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("recipes/list"))
                    .andExpect(content().string(containsString("public_3")))
                    .andExpect(content().string(not(allOf(
                            containsString("private_1"),
                            containsString("followers_2")
                    ))));
        }

        @Test
        @WithAnonymousUser
        void getAnyRecipe_byAnonymous_redirectsToLogin () throws Exception {

            mockMvc.perform(get("/recipes/1"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));

            mockMvc.perform(get("/recipes/2"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));

            mockMvc.perform(get("/recipes/3"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }

        @Test
        @WithUserDetails(
                value = "alibaba@gmail.com",
                userDetailsServiceBeanName = "userDetailsService",
                setupBefore = TestExecutionEvent.TEST_EXECUTION
        )
        void getPrivateRecipe_byLoggedUser_returnsForbidden () throws Exception {

            mockMvc.perform(get("/recipes/4"))
                    .andExpect(status().isForbidden());

            mockMvc.perform(get("/recipes/7"))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithUserDetails(
                value = "alibaba@gmail.com",
                userDetailsServiceBeanName = "userDetailsService",
                setupBefore = TestExecutionEvent.TEST_EXECUTION
        )
        void getAll_byLoggedUser_returnsOnlyRecipesWithGrantedAccess () throws Exception {

            final CurrentUser currentUser
                    = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            //noinspection OptionalGetWithoutIsPresent
            final UserEntity currentUserEntity
                    = userRepository.findUserById(currentUser.getId()).get();

            // noinspection ConstantConditions,unchecked
            List<RecipeCardVM> recipeCardVMList
                    = (List<RecipeCardVM>) mockMvc.perform(get("/recipes/all"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("recipes/list"))
                    .andExpect(model().attributeExists("recipeCards"))
                    .andReturn().getModelAndView().getModel().get("recipeCards");


            final Predicate<RecipeCardVM> excludeSelfOwned
                    = rc -> !rc.getAuthorId().equals(currentUser.getId());

            //noinspection OptionalGetWithoutIsPresent
            final Predicate<RecipeCardVM> excludePublic
                    = rc -> !recipeRepository
                    .findById(rc.getEntityId()).get()
                    .getVisibilityStatus()
                    .equals(VisibilityStatus.PUBLIC);

            final Predicate<RecipeCardVM> excludeGrantedAccessAsFollower
                    = rc -> {
                //noinspection OptionalGetWithoutIsPresent
                final RecipeEntity recipe = recipeRepository
                        .findById(rc.getEntityId()).get();

                if (recipe.getVisibilityStatus().equals(VisibilityStatus.FOLLOWERS)) {
                    return !recipe.getAuthor().getFollowers().contains(currentUserEntity);
                }

                return true;
            };

            assertTrue(recipeCardVMList.stream()
                               .filter(excludePublic)
                               .filter(excludeSelfOwned)
                               .filter(excludeGrantedAccessAsFollower)
                               .findAny()
                               .isEmpty());
        }

        @Test
        @WithUserDetails(
                value = "khanko@brat.bg",
                userDetailsServiceBeanName = "userDetailsService"
        )
        void getAll_byAdmin_returnsAllRecipes () throws Exception {

            // noinspection ConstantConditions,unchecked
            List<RecipeCardVM> recipeCardVMList
                    = (List<RecipeCardVM>) mockMvc.perform(get("/recipes/all"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("recipes/list"))
                    .andExpect(model().attributeExists("recipeCards"))
                    .andReturn().getModelAndView().getModel().get("recipeCards");

            final long expected = recipeRepository.count();
            final Long actual = (long) recipeCardVMList.size();

            assertEquals(expected, actual);
        }
    }
}