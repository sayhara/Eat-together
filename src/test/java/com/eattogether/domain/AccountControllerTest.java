package com.eattogether.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("SignUp Page")
    @Test
    void signUp_get() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));
    }

    @DisplayName("SignUp Value Error")
    @Test
    void signUp_wrong_value() throws Exception {
        mockMvc.perform(post("/sign-up")
        .param("nickname","gyuwon")
        .param("email","google@google.com")
        .param("password","12345678")
        .param("passwordRepeat","123456789")
        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));
    }

    @DisplayName("signUp Value Correct")
    @Test
    void signUp_correct_value() throws Exception {
        mockMvc.perform(post("/sign-up")
        .param("nickname","gyuwon")
        .param("email","google@google.com")
        .param("password","12345678")
        .param("passwordRepeat","12345678")
        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

}