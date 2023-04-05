package dev.thrako.receptaria.web.controller;

import dev.thrako.receptaria.ReceptariaApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = ReceptariaApplication.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void initUserRegistrationBM () {

    }

    @Test
    void getRegistrationForm () {

    }

    @Test
    void given_doRegisterWithValidData_shouldRedirectSuccessPage () throws Exception {

        mockMvc.perform(post("/registration").
                                param("firstName", "testFirst").
                                param("lastName", "testLast").
                                param("displayName", "testDisplayName").
                                param("email", "test@testmail.com").
                                param("password", "test1234").
                                param("confirmPassword", "test1234").
                                with(csrf())
                ).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/registration/success"));
    }

    @Test
    void given_doRegisterWithInvalidData_shouldSomething () throws Exception {

        mockMvc.perform(post("/registration")
                                .param("firstName", "")
                                .param("lastName", "")
                                .param("displayName", "")
                                .param("email", "testtestmailcom")
                                .param("password", "t")
                                .param("confirmPassword", "test1234")
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registration"));
    }

    @Test
    void getSuccessPage () {

    }

    @Test
    void getLoginForm () {

    }

    @Test
    void getOwnProfile () {

    }

    @Test
    void getUserProfile () {

    }
}