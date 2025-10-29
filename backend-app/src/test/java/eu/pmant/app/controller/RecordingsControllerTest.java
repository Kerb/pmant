package eu.pmant.app.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import eu.pmant.app.dto.Recording;
import eu.pmant.app.dto.RecordingsResponse;
import eu.pmant.app.dto.SessionData;
import eu.pmant.app.generated.jooq.tables.pojos.UserMeetings;
import eu.pmant.app.repository.MeetingsRepository;
import eu.pmant.app.service.SpeechRecognizeService;
import eu.pmant.app.session.SessionDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

class RecordingsControllerTest {

    private RecordingsController recordingsController;
    private SessionDataProvider sessionDataProvider;
    private MeetingsRepository meetingsRepository;
    private SpeechRecognizeService speechRecognizeService;

    @BeforeEach
    void setUp() {
        sessionDataProvider = Mockito.mock(SessionDataProvider.class);
        doReturn(SessionData.builder().userId(1L).login("foobar").build())
            .when(sessionDataProvider)
            .getSessionData();

        meetingsRepository = Mockito.mock(MeetingsRepository.class);

        speechRecognizeService = Mockito.mock(SpeechRecognizeService.class);
        Mockito.doReturn("speech").when(speechRecognizeService).recognizeSpeech(ArgumentMatchers.anyString());

        UserMeetings userMeetings = new UserMeetings(
            1L,
            1L,
            "Product Strategy Meeting",
            "Created",
            "FileName.ogg",
            "Path",
            45L,
            "speech",
            LocalDateTime.parse("2024-12-01T10:10:10"));
        doReturn(List.of(userMeetings))
            .when(meetingsRepository)
            .findMeetingsByUserId(ArgumentMatchers.any());

        recordingsController = new RecordingsController(meetingsRepository, sessionDataProvider, speechRecognizeService);
    }

    @Test
    void testRecordingsEndpointReturnsOkStatus() {
        ResponseEntity<RecordingsResponse> response = recordingsController.recordingsList();
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Recordings endpoint should return OK status");
    }

    @Test
    void testRecordingsEndpointReturnsListOfRecordings() {
        ResponseEntity<RecordingsResponse> response = recordingsController.recordingsList();
        RecordingsResponse recordingsResponse = response.getBody();

        assertNotNull(recordingsResponse, "Response body should not be null");
        assertTrue(recordingsResponse.isSuccess(), "Response should be marked as successful");
        assertNotNull(recordingsResponse.getRecordings(), "Recordings list should not be null");
        assertFalse(recordingsResponse.getRecordings().isEmpty(), "Recordings list should not be empty");
        assertEquals(1, recordingsResponse.getRecordings().size(), "Should return exactly 4 recordings");
    }

    @Test
    void testRecordingsContainExpectedData() {
        ResponseEntity<RecordingsResponse> response = recordingsController.recordingsList();
        List<Recording> recordings = response.getBody().getRecordings();

        // Check first recording
        Recording firstRecording = recordings.get(0);
        assertEquals(1L, firstRecording.getId());
        assertEquals("Product Strategy Meeting", firstRecording.getTitle());
        assertEquals("01.12.2024", firstRecording.getDate());
        assertEquals("10:10", firstRecording.getTime());
        assertEquals("xx min", firstRecording.getDuration());//todo реализация
        assertEquals("Created", firstRecording.getStatus());
    }

    @Test
    void testAllRecordingsHaveRequiredFields() {
        ResponseEntity<RecordingsResponse> response = recordingsController.recordingsList();
        RecordingsResponse recordingsResponse = response.getBody();
        List<Recording> recordings = recordingsResponse.getRecordings();

        assertTrue(recordingsResponse.isSuccess(), "Response should be marked as successful");

        for (Recording recording : recordings) {
            assertNotNull(recording.getId(), "Each recording should have an ID");
            assertNotNull(recording.getTitle(), "Each recording should have a title");
            assertNotNull(recording.getDate(), "Each recording should have a date");
            assertNotNull(recording.getTime(), "Each recording should have a time");
            assertNotNull(recording.getDuration(), "Each recording should have a duration");
            assertNotNull(recording.getStatus(), "Each recording should have a status");
        }
    }
}