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

    public Meeting createMeetingDescriptionForm(MeetingDescriptionForm descriptionForm) {

        Meeting meeting=Meeting.builder()
                .short_note(descriptionForm.getShort_note())
                .long_note(descriptionForm.getLong_note())
                .build();

        Meeting saveMeeting = meetingRepository.save(meeting);

        return saveMeeting;

//        meeting.setShort_note(descriptionForm.getShort_note());
//        meeting.setLong_note(descriptionForm.getLong_note());
//        meetingRepository.save(meeting);
//
//        return meeting;

    }

    public void updateMeetingDescription(Meeting meeting, @Valid MeetingDescriptionForm descriptionForm) {
        meeting.setShort_note(descriptionForm.getShort_note());
        meeting.setLong_note(descriptionForm.getLong_note());
        meetingRepository.save(meeting);
    }
}
