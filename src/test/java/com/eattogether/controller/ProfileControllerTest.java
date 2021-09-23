package com.eattogether.controller;

import com.eattogether.domain.Account;
import com.eattogether.domain.Tag;
import com.eattogether.domain.Zone;
import com.eattogether.dto.SignUpForm;
import com.eattogether.dto.TagForm;
import com.eattogether.dto.ZoneForm;
import com.eattogether.repository.AccountRepository;
import com.eattogether.repository.TagRepository;
import com.eattogether.repository.ZoneRepository;
import com.eattogether.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.commons.lang.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProfileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    ZoneRepository zoneRepository;

    private Zone testZone=Zone.builder().part1("테스트1").part2("테스트2").part3("테스트3").build();

    @BeforeEach
    void beforeEach(){
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("gyuwon");
        signUpForm.setEmail("google@google.com");
        signUpForm.setPassword("123456789");
        signUpForm.setPasswordRepeat("123456789");
        accountService.makeAccount(signUpForm);
        zoneRepository.save(testZone);
    }

    @AfterEach
    void afterEach(){
        accountRepository.deleteAll();
        zoneRepository.deleteAll();
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

    // 관심주제 테스트
    @WithUserDetails(value = "gyuwon", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("계정에 태그 수정 폼")
    @Test
    void updateTagsForm() throws Exception{
        mockMvc.perform(get("/settings/tags"))
                .andExpect(view().name("settings/tags"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("tags"));
    }

    @WithUserDetails(value = "gyuwon", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("계정에 태그 추가")
    @Test
    void addTag() throws Exception{

        TagForm tagForm=new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post("/settings/tags/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm)) // 데이터가 파라미터가 아닌 본문으로 들어옴
                .with(csrf()))
                .andExpect(status().isOk());

        Tag newTag = tagRepository.findByTitle("newTag");
        assertNotNull(newTag);
//        assertTrue(accountRepository.findByNickname("gyuwon").getTags().contains(newTag));
//        accountRepository.findByNickname("gyuwon")까지는 리파지토리를 통해 @Transactional에서 처리
//        하지만 이후의 처리 상태는 detached -> no Session이라도 뜸
//        따라서, 이 테스트코드 내에 @Transactional이 있어야 persist 상태가 가능
        assertTrue(accountRepository.findByNickname("gyuwon").getTags().contains(newTag));
    }

    @WithUserDetails(value = "gyuwon", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("계정에 태그 삭제")
    @Test
    void removeTag() throws Exception{
        Account gyuwon = accountRepository.findByNickname("gyuwon");
        Tag newTag=tagRepository.save(Tag.builder().title("newTag").build());
        accountService.addTag(gyuwon,newTag);

        assertTrue(gyuwon.getTags().contains(newTag));

        TagForm tagForm=new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post("/settings/tags/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm))
                .with(csrf()))
                .andExpect(status().isOk());

        assertFalse(gyuwon.getTags().contains(newTag));
    }
    
    //지역정보 테스트
    @WithUserDetails(value="gyuwon", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("계정에 지역정보 수정 폼")
    @Test
    void updateZoneForm() throws Exception{
        mockMvc.perform(get("/settings/zones"))
                .andExpect(view().name("settings/zones"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("zones"))
                .andExpect(model().attributeExists("whitelist"));
    }

    @WithUserDetails(value = "gyuwon", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("계정에 지역정보 추가")
    @Test
    void addZone() throws Exception{

        ZoneForm zoneForm=new ZoneForm();
        zoneForm.setZoneName(testZone.toString());

        mockMvc.perform(post("/settings/zones/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(zoneForm))
                .with(csrf()))
                .andExpect(status().isOk());

        Zone newZone=zoneRepository.findByPart1AndPart3(testZone.getPart1(),testZone.getPart3());
        assertNotNull(newZone);

        assertTrue(accountRepository.findByNickname("gyuwon").getZones().contains(newZone));
    }

    @WithUserDetails(value = "gyuwon", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("계정에 지역정보 삭제")
    @Test
    void removeZone() throws Exception{

        Account gyuwon=accountRepository.findByNickname("gyuwon");
        Zone zone = zoneRepository.findByPart1AndPart3(testZone.getPart1(), testZone.getPart3());
        accountService.addZone(gyuwon,zone);

        assertTrue(gyuwon.getZones().contains(testZone));

        ZoneForm zoneForm=new ZoneForm();
        zoneForm.setZoneName(testZone.toString());

        mockMvc.perform(post("/settings/zones/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(zoneForm))
                .with(csrf()))
                .andExpect(status().isOk());

        assertFalse(gyuwon.getZones().contains(zone));
    }


}