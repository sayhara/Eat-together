package com.eattogether.controller;

import com.eattogether.config.AuthUser;
import com.eattogether.domain.Account;
import com.eattogether.domain.Tag;
import com.eattogether.domain.Zone;
import com.eattogether.dto.*;
import com.eattogether.repository.AccountRepository;
import com.eattogether.repository.TagRepository;
import com.eattogether.repository.ZoneRepository;
import com.eattogether.service.AccountService;
import com.eattogether.validator.NicknameFormValidator;
import com.eattogether.validator.PasswordFormValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final PasswordFormValidator passwordFormValidator;
    private final NicknameFormValidator nicknameFormValidator;
    private final TagRepository tagRepository;
    private final ZoneRepository zoneRepository;
    private final ObjectMapper objectMapper;

    @InitBinder("passwordForm")
    public void passwordFormBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(passwordFormValidator);
    }

    @InitBinder("nicknameForm")
    public void nicknameFormBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(nicknameFormValidator);
    }

    @GetMapping("/profile/{nickname}")
    public String ProfilePage(@AuthUser Account account,
                              @PathVariable String nickname, Model model){
        Account byNickname = accountRepository.findByNickname(nickname);

        if(byNickname==null){
            throw new IllegalArgumentException(nickname+"에 해당하는 사용자가 없습니다.");
        }

        model.addAttribute(byNickname);
        model.addAttribute("isOwner",byNickname.equals(account));
        return "account/profile";
    }

    @GetMapping("/settings/profile")
    public String profileSetting(@AuthUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new Profile(account));
        // model.addAttribute(String name, Object value)
        // model.addAttribute(Object value)
        return "settings/profile";
    }

    @PostMapping("/settings/profile")
    public String profileUpdate(@AuthUser Account account, Profile profile,
                                Errors errors, Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "settings/profile";
        }
        accountService.profileUpdate(account,profile);
        attributes.addFlashAttribute("message","프로필을 수정했습니다.");
        return "redirect:/settings/profile";
    }

    @GetMapping("/settings/password")
    public String passwordSettings(@AuthUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());
        return "settings/password";
    }

    @PostMapping("/settings/password")
    public String passwordUpdate(@AuthUser Account account, @Validated PasswordForm passwordForm,
                                 Errors errors, Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "settings/password";
        }
        accountService.passwordUpdate(account,passwordForm);
        attributes.addFlashAttribute("message","패스워드를 수정했습니다.");
        return "redirect:/settings/password";
    }

    @GetMapping("/settings/alarm")
    public String alarmSettings(@AuthUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new Alarm(account));
        return "settings/alarm";
    }

    @PostMapping("/settings/alarm")
    public String alarmUpdate(@AuthUser Account account, Alarm alarm, Errors errors,
                                      Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "settings/alarm";
        }

        accountService.alarmUpdate(account,alarm);
        attributes.addFlashAttribute("message","알림 설정을 변경했습니다.");
        return "redirect:/settings/alarm";
    }

    @GetMapping("/settings/nickname")
    public String nicknameSettings(@AuthUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new NicknameForm());
        return "settings/nickname";
    }

    @PostMapping("/settings/nickname")
    public String nicknameUpdate(@AuthUser Account account, @Validated NicknameForm nicknameForm,
                                 Errors errors, Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "settings/nickname";
        }

        accountService.nicknameUpdate(account,nicknameForm);
        accountService.login(account); // 로그아웃 필요없이 닉네임 바로 반영
        attributes.addFlashAttribute("message","닉네임을 변경했습니다.");
        return "redirect:/settings/nickname";
    }

    @GetMapping("/settings/tags")
    public String updateTags(@AuthUser Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);
        Set<Tag> tags = accountService.getTags(account);
        model.addAttribute("tags",tags.stream().map(Tag::getTitle).collect(Collectors.toList()));

        List<String> allTags = tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("whitelist",objectMapper.writeValueAsString(allTags)); // JSON형식으로 변환

        return "settings/tags";
    }

    @PostMapping("/settings/tags/add")
    @ResponseBody
    public ResponseEntity addTag(@AuthUser Account account, @RequestBody TagForm tagForm){
        String title = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(title);

        if(tag==null){
            tag = tagRepository.save(Tag.builder().title(tagForm.getTagTitle()).build());
        }

        accountService.addTag(account,tag);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/settings/tags/remove")
    @ResponseBody
    public ResponseEntity removeTag(@AuthUser Account account, @RequestBody TagForm tagForm){
        String title = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(title);

        if(tag==null){
            return ResponseEntity.badRequest().build();
        }

        accountService.removeTag(account,tag);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/settings/zones")
    public String zoneSettings(@AuthUser Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);

        Set<Zone> zones=accountService.getZones(account);
        model.addAttribute("zones",zones.stream().map(Zone::toString).collect(Collectors.toList()));

        List<String> allZones=zoneRepository.findAll().stream().map(Zone::toString).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allZones)); // String 타입으로 변환

        return "settings/zones";

    }

    @PostMapping("/settings/zones/add")
    @ResponseBody
    public ResponseEntity addZone(@AuthUser Account account, @RequestBody ZoneForm zoneForm){

        Zone zone=zoneRepository.findByPart1AndPart3(zoneForm.getPart1Name(),zoneForm.getPart3Name());

        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.addZone(account,zone);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/settings/zones/remove")
    @ResponseBody
    public ResponseEntity removeZone(@AuthUser Account account, @RequestBody ZoneForm zoneForm){

        Zone zone = zoneRepository.findByPart1AndPart3(zoneForm.getPart1Name(), zoneForm.getPart3Name());

        if(zone==null){
            return ResponseEntity.badRequest().build();
        }

        accountService.removeZone(account,zone);
        return ResponseEntity.ok().build();
    }

}
