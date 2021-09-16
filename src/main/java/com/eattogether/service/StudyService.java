package com.eattogether.service;

import com.eattogether.domain.Account;
import com.eattogether.domain.Study;
import com.eattogether.domain.Zone;
import com.eattogether.dto.StudyCreatedEvent;
import com.eattogether.dto.StudyDescriptionForm;
import com.eattogether.dto.StudyUpdateEvent;
import com.eattogether.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.HashSet;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Study createNewStudy(Study study, Account account){

//        Meeting meeting = Meeting.builder()
//                .url(meetingForm.getUrl())
//                .title(meetingForm.getTitle())
//                .short_note(meetingForm.getShort_note())
//                .long_note(meetingForm.getLong_note())
//                .build();

        Study saveStudy = studyRepository.save(study);
        saveStudy.addManager(account);

        return saveStudy;
    }
    
    public Study getStudy(String url){

        Study study = studyRepository.findByUrl(url);

        if(study ==null){
            throw new IllegalArgumentException(url+"에 해당하는 모임이 없습니다.");
        }
        return study;
    }



    public Study getStudyUpdate(Account account, String url)
            throws AccessDeniedException {

        Study study = getStudy(url);

        if(!account.isManagerOf(study)){
            throw new AccessDeniedException("해당 기능 사용 불가능합니다.");
        }

        return study;
    }

    public void updateStudyDescription(Study study, @Valid StudyDescriptionForm descriptionForm) {
        study.setShort_note(descriptionForm.getShort_note());
        study.setLong_note(descriptionForm.getLong_note());
        eventPublisher.publishEvent(new StudyUpdateEvent(study,"스터디 소개를 수정했습니다."));
    }

    public void updateStudyImage(Study study, String image) {
        study.setImage(image);
    }

    public void enableStudyBanner(Study study) {
        study.setUseBanner(true);
    }

    public void disableStudyBanner(Study study) {
        study.setUseBanner(false);
    }

    public Study getStudyUpdateStatus(Account account, String url) throws AccessDeniedException {
        Study study = studyRepository.findByUrl(url);
        checkIfExistingStudy(url, study);
        checkIfManager(account, study);
        return study;
    }

    private void checkIfManager(Account account, Study study) throws AccessDeniedException {
        if(!account.isManagerOf(study)){
            throw new AccessDeniedException("해당 기능 사용 불가능");
        }
    }

    private void checkIfExistingStudy(String url, Study study) {
        if(study ==null){
            throw new IllegalArgumentException(url+"에 해당하는 스터디가 없습니다.");
        }
    }

    public void publish(Study study) { // 스터디 공개
        study.publish();
        this.eventPublisher.publishEvent(new StudyCreatedEvent(study));
    }

    public void close(Study study){
        study.close();
        eventPublisher.publishEvent(new StudyUpdateEvent(study,"스터디를 종료했습니다."));
    }

    public void startRecruit(Study study) {
        study.startRecruit();
        eventPublisher.publishEvent(new StudyUpdateEvent(study,"팀원 모집을 시작합니다."));
    }

    public void stopRecruit(Study study) {
        study.stopRecruit();
        eventPublisher.publishEvent(new StudyUpdateEvent(study,"팀원 모집을 중단했습니다."));
    }

    public boolean isValidUrl(String newUrl) {
        String regex = "^[a-zA-Z0-9_-]{5,20}$";
        if(!newUrl.matches(regex)){
            return false;
        }
        return !studyRepository.existsByUrl(newUrl);
    }

    public void updateStudyUrl(Study study, String newUrl) {
        study.setUrl(newUrl);
    }

    public boolean isValidTitle(String newTitle) {
        return newTitle.length()<=50;
    }

    public void updateStudyTitle(Study study, String newTitle) {
        study.setTitle(newTitle);
    }

    public void remove(Study study) {
        if(study.isRemovable()){
            studyRepository.delete(study);
        } else{
            throw new IllegalArgumentException("스터디를 삭제할 수 없습니다.");
        }
    }

    public void addMember(Study study, Account account) {
        study.addMember(account);
    }

    public void removeMember(Study study, Account account) {
        study.removeMember(account);
    }

    public Study getStudyToEnroll(String url) {
        Study study=studyRepository.findStudyOnlyByUrl(url);
        checkIfExistingStudy(url,study);
        return study;
    }

    public Study getStudyUpdateZone(Account account, String url) throws AccessDeniedException {
        Study study = studyRepository.findStudyWithZonesByUrl(url);
        checkIfExistingStudy(url,study);
        checkIfManager(account,study);
        return study;
    }

    public void addZone(Study study, Zone zone) {
        study.getZones().add(zone);
    }

    public void removeZone(Study study, Zone zone) {
        study.getZones().remove(zone);
    }

    public void generateTestStudies(Account account) {
        for(int i=0;i<30;i++){
            String randomValue= RandomString.make(5);
            Study study = Study.builder()
                    .title("테스트 스터디" + randomValue)
                    .url("test-" + randomValue)
                    .short_note("test")
                    .long_note("테스트용 스터디")
                    .managers(new HashSet<>())
                    .build();
            study.publish();
            createNewStudy(study, account);
        }
    }
}
