package dev.thrako.receptaria.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void getRegistrationForm_byAnonymous_returnsProperView () throws Exception {

        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registration"));
    }

    @Test
    @WithMockUser
    void getRegistrationForm_byUser_returnsStatusForbidden () throws Exception {

        mockMvc.perform(get("/registration"))
                .andExpect(status().isForbidden());
    }

    @Test
    void doRegister_withValidData_shouldRedirectSuccessPage () throws Exception {

        mockMvc.perform(post("/registration")
                                .param("firstName", "testFirst")
                                .param("lastName", "testLast")
                                .param("displayName", "testDisplayName")
                                .param("email", "tester@testmail.com")
                                .param("password", "test1234")
                                .param("confirmPassword", "test1234")
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("displayName"))
                .andExpect(redirectedUrl("/registration/success"));
    }

    @Test
    void doRegister_withInvalidData_returnsRegistrationFormWithErrors () throws Exception {

        mockMvc.perform(post("/registration")
                                .param("firstName", "")
                                .param("lastName", "")
                                .param("displayName", "")
                                .param("email", "testtestmailcom")
                                .param("password", "t")
                                .param("confirmPassword", "test1234")
                                .with(csrf())
                )
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeHasErrors("userRegistrationBM"))
                .andExpect(view().name("auth/registration"));
    }

    @Test
    @WithAnonymousUser
    void getSuccessPage_whenRedirectedAfterRegistration_returnsCorrectView () throws Exception {

        final String redirectedUrl
                = mockMvc.perform(post("/registration")
                                  .param("firstName", "testFirst")
                                  .param("lastName", "testLast")
                                  .param("displayName", "testDisplayName")
                                  .param("email", "tester2@testmail.com")
                                  .param("password", "test1234")
                                  .param("confirmPassword", "test1234")
                                  .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("displayName", "testDisplayName"))
                .andReturn().getResponse().getHeader("Location");

        assert redirectedUrl != null;
        mockMvc.perform(get(redirectedUrl).flashAttr("displayName", "testDisplayName"))
                .andExpect(model().attribute("displayName", "testDisplayName"))
                .andExpect(content().string(allOf(
                        containsString("testDisplayName"),
                        containsString("Congratulations")
                )));
    }

    @Test
    @WithAnonymousUser
    void getSuccessPage_returnsCorrectView () throws Exception {

        mockMvc.perform(get("/registration/success"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registration-success"))
                .andExpect(content().string(not(containsString("Congratulations"))));
    }


    @Test
    @WithAnonymousUser
    void getLoginForm_byAnonymous_returnsCorrectView () throws Exception {

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    @WithMockUser
    void getLoginForm_byLoggedUser_returnsForbiddenStatus () throws Exception {

        mockMvc.perform(get("/login"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void getOwnProfile_byAnonymous_redirectsToLoginPage () throws Exception {

        mockMvc.perform(get("/users/me"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithUserDetails(value = "alibaba@gmail.com", userDetailsServiceBeanName = "userDetailsService")
    void getOwnProfile_byLoggedUser_returnsCorrectView () throws Exception {

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/owner"))
                .andExpect(model().attribute("displayName", "Ali Baba"));
    }

    @Test
    @WithAnonymousUser
    void getUserProfileOfExistingUser_byAnonymousUser_returnForbiddenStatus () throws Exception {

        mockMvc.perform(get("/users/{id}", "id=4"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithUserDetails(
            value = "alibaba@gmail.com",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void getUserProfileOfExistingUser_byLoggedUser_returnCorrectView () throws Exception {

        mockMvc.perform(get("/users/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/other"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("displayName", "Ali Baba"))
                .andExpect(model().attribute("userId", 1L));
    }
}