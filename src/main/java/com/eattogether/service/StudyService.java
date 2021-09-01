package com.eattogether.service;

import com.eattogether.domain.Account;
import com.eattogether.domain.Study;
import com.eattogether.dto.StudyDescriptionForm;
import com.eattogether.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

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

//    public Meeting createMeetingDescriptionForm(MeetingDescriptionForm descriptionForm) {
//
//        Meeting meeting=Meeting.builder()
//                .short_note(descriptionForm.getShort_note())
//                .long_note(descriptionForm.getLong_note())
//                .build();
//
//        Meeting saveMeeting = meetingRepository.save(meeting);
//
//        return saveMeeting;
//
////        meeting.setShort_note(descriptionForm.getShort_note());
////        meeting.setLong_note(descriptionForm.getLong_note());
////        meetingRepository.save(meeting);
////
////        return meeting;
//
//    }

    public void updateStudyDescription(Study study, @Valid StudyDescriptionForm descriptionForm) {
        study.setShort_note(descriptionForm.getShort_note());
        study.setLong_note(descriptionForm.getLong_note());
        studyRepository.save(study);
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

    public void publish(Study study) {
        study.publish();
    }

    public void close(Study study){
        study.close();
    }

    public void startRecruit(Study study) {
        study.startRecruit();
    }

    public void stopRecruit(Study study) {
        study.stopRecruit();
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
}
