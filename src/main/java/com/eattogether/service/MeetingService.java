package com.eattogether.service;

import com.eattogether.domain.Account;
import com.eattogether.domain.Meeting;
import com.eattogether.dto.MeetingDescriptionForm;
import com.eattogether.dto.MeetingForm;
import com.eattogether.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    public Meeting createNewMeeting(Meeting meeting, Account account){

//        Meeting meeting = Meeting.builder()
//                .url(meetingForm.getUrl())
//                .title(meetingForm.getTitle())
//                .short_note(meetingForm.getShort_note())
//                .long_note(meetingForm.getLong_note())
//                .build();
        Meeting saveMeeting = meetingRepository.save(meeting);
        saveMeeting.addManager(account);

        return saveMeeting;
    }
    
    public Meeting getMeeting(String url){

        Meeting meeting = meetingRepository.findByUrl(url);

        if(meeting==null){
            throw new IllegalArgumentException(url+"에 해당하는 모임이 없습니다.");
        }
        return meeting;
    }



    public Meeting getMeetingUpdate(Account account, String url)
            throws AccessDeniedException {

        Meeting meeting = getMeeting(url);

        if(!account.isManagerOf(meeting)){
            throw new AccessDeniedException("해당 기능 사용 불가능합니다.");
        }

        return meeting;
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

    public void updateMeetingDescription(Meeting meeting, @Valid MeetingDescriptionForm descriptionForm) {
        meeting.setShort_note(descriptionForm.getShort_note());
        meeting.setLong_note(descriptionForm.getLong_note());
        meetingRepository.save(meeting);
    }

    public void updateMeetingImage(Meeting meeting, String image) {
        meeting.setImage(image);
    }

    public void enableMeetingBanner(Meeting meeting) {
        meeting.setUseBanner(true);
    }

    public void disableMeetingBanner(Meeting meeting) {
        meeting.setUseBanner(false);
    }

    public Meeting getMeetingUpdateStatus(Account account, String url) throws AccessDeniedException {
        Meeting meeting = meetingRepository.findByUrl(url);
        checkIfExistingMeeting(url,meeting);
        checkIfManager(account,meeting);
        return meeting;
    }

    private void checkIfManager(Account account, Meeting meeting) throws AccessDeniedException {
        if(!account.isManagerOf(meeting)){
            throw new AccessDeniedException("해당 기능 사용 불가능");
        }
    }

    private void checkIfExistingMeeting(String url, Meeting meeting) {
        if(meeting==null){
            throw new IllegalArgumentException(url+"에 해당하는 모임이 없습니다.");
        }
    }

    public void publish(Meeting meeting) {
        meeting.publish();
    }

    public void close(Meeting meeting){
        meeting.close();
    }

    public void startRecruit(Meeting meeting) {
        meeting.startRecruit();
    }

    public void stopRecruit(Meeting meeting) {
        meeting.stopRecruit();
    }

    public boolean isValidUrl(String newUrl) {
        String regex = "^[a-zA-Z0-9_-]{5,20}$";
        if(!newUrl.matches(regex)){
            return false;
        }
        return !meetingRepository.existsByUrl(newUrl);
    }

    public void updateMeetingUrl(Meeting meeting, String newUrl) {
        meeting.setUrl(newUrl);
    }

    public boolean isValidTitle(String newTitle) {
        return newTitle.length()<=50;
    }

    public void updateMeetingTitle(Meeting meeting, String newTitle) {
        meeting.setTitle(newTitle);
    }

    public void remove(Meeting meeting) {
        if(meeting.isRemovable()){
            meetingRepository.delete(meeting);
        } else{
            throw new IllegalArgumentException("모임을 삭제할 수 없습니다.");
        }
    }

    public void addMember(Meeting meeting, Account account) {
        meeting.addMember(account);
    }

    public void removeMember(Meeting meeting, Account account) {
        meeting.removeMember(account);
    }
}
