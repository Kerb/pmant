package eu.pmant.app.service;


import java.util.Optional;

public interface MinutesOfMeetingExtractService {

    Optional<String> extractMinutesOfMeeting(String meetingText);
}
