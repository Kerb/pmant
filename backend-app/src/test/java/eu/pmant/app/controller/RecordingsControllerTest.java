package eu.pmant.app.controller;

import static org.junit.jupiter.api.Assertions.*;

import eu.pmant.app.dto.Recording;
import eu.pmant.app.dto.RecordingsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

class RecordingsControllerTest {

    private RecordingsController recordingsController;

    @BeforeEach
    void setUp() {
        recordingsController = new RecordingsController();
    }

    @Test
    void testRecordingsEndpointReturnsOkStatus() {
        ResponseEntity<RecordingsResponse> response = recordingsController.recordings();
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Recordings endpoint should return OK status");
    }

    @Test
    void testRecordingsEndpointReturnsListOfRecordings() {
        ResponseEntity<RecordingsResponse> response = recordingsController.recordings();
        RecordingsResponse recordingsResponse = response.getBody();
        
        assertNotNull(recordingsResponse, "Response body should not be null");
        assertTrue(recordingsResponse.isSuccess(), "Response should be marked as successful");
        assertNotNull(recordingsResponse.getRecordings(), "Recordings list should not be null");
        assertFalse(recordingsResponse.getRecordings().isEmpty(), "Recordings list should not be empty");
        assertEquals(4, recordingsResponse.getRecordings().size(), "Should return exactly 4 recordings");
    }

    @Test
    void testRecordingsContainExpectedData() {
        ResponseEntity<RecordingsResponse> response = recordingsController.recordings();
        List<Recording> recordings = response.getBody().getRecordings();
        
        // Check first recording
        Recording firstRecording = recordings.get(0);
        assertEquals(1L, firstRecording.getId());
        assertEquals("Product Strategy Meeting", firstRecording.getTitle());
        assertEquals("2025-01-20", firstRecording.getDate());
        assertEquals("10:00 AM", firstRecording.getTime());
        assertEquals("45 min", firstRecording.getDuration());
        assertEquals(8, firstRecording.getParticipants());
        assertEquals("Completed", firstRecording.getStatus());
        
        // Check last recording
        Recording lastRecording = recordings.get(3);
        assertEquals(4L, lastRecording.getId());
        assertEquals("Team Standup", lastRecording.getTitle());
        assertEquals("2025-01-18", lastRecording.getDate());
        assertEquals("9:00 AM", lastRecording.getTime());
        assertEquals("15 min", lastRecording.getDuration());
        assertEquals(10, lastRecording.getParticipants());
        assertEquals("Completed", lastRecording.getStatus());
    }

    @Test
   void testAllRecordingsHaveRequiredFields() {
        ResponseEntity<RecordingsResponse> response = recordingsController.recordings();
        RecordingsResponse recordingsResponse = response.getBody();
       List<Recording> recordings = recordingsResponse.getRecordings();
        
        assertTrue(recordingsResponse.isSuccess(), "Response should be marked as successful");
        
        for (Recording recording : recordings) {
            assertNotNull(recording.getId(), "Each recording should have an ID");
            assertNotNull(recording.getTitle(), "Each recording should have a title");
            assertNotNull(recording.getDate(), "Each recording should have a date");
            assertNotNull(recording.getTime(), "Each recording should have a time");
            assertNotNull(recording.getDuration(), "Each recording should have a duration");
            assertNotNull(recording.getParticipants(), "Each recording should have participant count");
            assertNotNull(recording.getStatus(),"Each recording should have a status");
        }
    }
}