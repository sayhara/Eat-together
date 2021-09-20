package com.eattogether.controller;

import com.eattogether.domain.Account;
import com.eattogether.dto.SignUpForm;
import com.eattogether.repository.AccountRepository;
import com.eattogether.service.AccountService;
import com.mysema.commons.lang.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach(){
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("gyuwon");
        signUpForm.setEmail("google@google.com");
        signUpForm.setPassword("123456789");
        signUpForm.setPasswordRepeat("123456789");
        accountService.makeAccount(signUpForm);
    }

    @AfterEach
    void afterEach(){
        accountRepository.deleteAll();
    }

    /*
    테스트 하기 전에 gyuwon이라는 계정이 이미 저장이 되어야 한다.
    @BeforeEach() & @WithUserDetails()를 통해 값을 등록해주면 된다.

     */

    @WithUserDetails(value = "gyuwon", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("프로필 수정 폼")
    @Test
    void updateProfileForm() throws Exception{
        mockMvc.perform(get("/settings/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }

    @WithUserDetails(value = "gyuwon", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("프로필 수정 - 입력값 정상")
    @Test
    void updateProfile() throws Exception{
        String bio="간단한 소개를 수정하는 경우";
        mockMvc.perform(post("/settings/profile")
                .param("bio",bio)
                .with(csrf())) // FORM 데이터의 요청시 csrf() 토큰 필요
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("message"));

        Account gyuwon = accountRepository.findByNickname("gyuwon");
        assertEquals(bio,gyuwon.getBio());
    }

    @WithUserDetails(value = "gyuwon", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("프로필 수정 - 입력값 에러")
    @Test
    void updateProfile_error() throws Exception{
        String bio="=======================소개가 35자가 넘어가는 경우" +
                "======================================================소개가 35자가 넘어가는 경우\" +\n" +
                "                \"===============================";
        mockMvc.perform(post("/settings/profile")
                .param("bio",bio)
                .with(csrf())) // FORM 데이터의 요청시 csrf() 토큰 필요
                .andExpect(status().isOk())
                .andExpect(view().name("settings/profile"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account gyuwon = accountRepository.findByNickname("gyuwon");
        assertNull(gyuwon.getBio());
    }

    @WithUserDetails(value = "gyuwon", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("패스워드 수정 폼")
    @Test
    void updatePasswordForm() throws Exception{
        mockMvc.perform(get("/settings/password"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"));
    }

    @WithUserDetails(value = "gyuwon", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("패스워드 수정 - 입력값 정상")
    @Test
    void updatePassword() throws Exception{
        String newPassword="11223344";
        String newPasswordRepeat="11223344";
        mockMvc.perform(post("/settings/password")
                    .param("newPassword",newPassword)
                    .param("newPasswordRepeat",newPasswordRepeat)
                    .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/settings/password"))
                    .andExpect(flash().attributeExists("message"));

        Account gyuwon = accountRepository.findByNickname("gyuwon");
        assertTrue(passwordEncoder.matches(newPassword,gyuwon.getPassword()));
    }

    @WithUserDetails(value = "gyuwon", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("패스워드 수정 - 입력값 에러")
    @Test
    void updatePassword_error() throws Exception{
        String newPassword="11223344";
        String newPasswordRepeat="22334455";
        mockMvc.perform(post("/settings/password")
                .param("newPasword",newPassword)
                .param("newPasswordRepeat",newPasswordRepeat)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful()); // 200에러 발생

    }

}