package eu.pmant.app.async;

import eu.pmant.app.dto.MeetingStatus;
import eu.pmant.app.generated.jooq.tables.pojos.UserMeetings;
import eu.pmant.app.repository.MeetingsRepository;
import eu.pmant.app.service.MinutesOfMeetingExtractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncMinutesOfMemoryExtractServiceTest {

    @Mock
    private MeetingsRepository meetingsRepository;

    @Mock
    private MinutesOfMeetingExtractService minutesOfMeetingExtractService;

    @InjectMocks
    private AsyncMinutesOfMemoryExtractService service;

    @Captor
    private ArgumentCaptor<UserMeetings> userMeetingsCaptor;

    private UserMeetings userMeeting;

    @BeforeEach
    void setUp() {
        userMeeting = new UserMeetings();
        userMeeting.setRecordingId(1L);
        userMeeting.setStatus(MeetingStatus.TRANSCRIBATION_SUCCESS.name());
        userMeeting.setSpeech("This is a test speech.");
    }

    @Test
    void startMomExtractionBatch_shouldProcessMeetingAndSucceed() {
        // Given
        when(meetingsRepository.findMeetingsToExtractMinutesOfMeeting(anyInt())).thenReturn(List.of(userMeeting));
        when(minutesOfMeetingExtractService.extractMinutesOfMeeting(anyString())).thenReturn(Optional.of("Extracted MoM"));

        // When
        service.startMomExtractionBatch();

        // Then
        verify(meetingsRepository, times(2)).updateStatusAndMoM(userMeetingsCaptor.capture());
        List<UserMeetings> capturedMeetings = userMeetingsCaptor.getAllValues();

        // Check finishSuccessfully call
        UserMeetings finalMeeting = capturedMeetings.get(1);
        assertEquals(MeetingStatus.MINUTES_OF_MEETING_SUCCESS.name(), finalMeeting.getStatus());
        assertEquals("Extracted MoM", finalMeeting.getMinutesOfMeeting());
    }

    @Test
    void startMomExtractionBatch_shouldHandleEmptySpeechAndFail() {
        // Given
        userMeeting.setSpeech("");
        when(meetingsRepository.findMeetingsToExtractMinutesOfMeeting(anyInt())).thenReturn(List.of(userMeeting));
        when(minutesOfMeetingExtractService.extractMinutesOfMeeting(anyString())).thenReturn(Optional.of(""));

        // When
        service.startMomExtractionBatch();

        // Then
        verify(meetingsRepository, times(2)).updateStatusAndMoM(userMeetingsCaptor.capture());
        List<UserMeetings> capturedMeetings = userMeetingsCaptor.getAllValues();

        // Check finishSuccessfully call (which leads to fail status)
        UserMeetings finalMeeting = capturedMeetings.get(1);
        assertEquals(MeetingStatus.MINUTES_OF_MEETING_FAIL.name(), finalMeeting.getStatus());
        assertEquals("", finalMeeting.getMinutesOfMeeting());
    }

    @Test
    void startMomExtractionBatch_shouldHandleExtractionExceptionAndFail() {
        // Given
        when(meetingsRepository.findMeetingsToExtractMinutesOfMeeting(anyInt())).thenReturn(List.of(userMeeting));
        when(minutesOfMeetingExtractService.extractMinutesOfMeeting(anyString())).thenThrow(new RuntimeException("Extraction failed"));

        // When
        service.startMomExtractionBatch();

        // Then
        verify(meetingsRepository, times(2)).updateStatusAndMoM(userMeetingsCaptor.capture());
        List<UserMeetings> capturedMeetings = userMeetingsCaptor.getAllValues();

        // Check finishWithError call
        UserMeetings finalMeeting = capturedMeetings.get(1);
        assertEquals(MeetingStatus.MINUTES_OF_MEETING_FAIL.name(), finalMeeting.getStatus());
    }

    @Test
    void startMomExtractionBatch_shouldDoNothingWhenNoMeetingsFound() {
        // Given
        when(meetingsRepository.findMeetingsToExtractMinutesOfMeeting(anyInt())).thenReturn(Collections.emptyList());

        // When
        service.startMomExtractionBatch();

        // Then
        verify(meetingsRepository, never()).updateStatusAndMoM(any());
        verify(minutesOfMeetingExtractService, never()).extractMinutesOfMeeting(anyString());
    }
}