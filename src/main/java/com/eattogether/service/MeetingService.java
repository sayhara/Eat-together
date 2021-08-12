package com.eattogether.service;

import com.eattogether.domain.Account;
import com.eattogether.domain.Meeting;
import com.eattogether.dto.MeetingForm;
import com.eattogether.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    public Meeting createNewMeeting(Meeting meeting, MeetingForm meetingForm, Account account){

//        Meeting meeting = Meeting.builder()
//                .url(meetingForm.getUrl())
//                .title(meetingForm.getTitle())
//                .short_note(meetingForm.getShort_note())
//                .long_note(meetingForm.getLong_note())
//                .build();

        meeting.setUrl(meeting.getUrl());
        meeting.setTitle(meetingForm.getTitle());
        meeting.setShort_note(meetingForm.getShort_note());
        meeting.setLong_note(meetingForm.getLong_note());
        Meeting saveMeeting = meetingRepository.save(meeting);

        saveMeeting.setManager(account);

        return saveMeeting;
    }

}
