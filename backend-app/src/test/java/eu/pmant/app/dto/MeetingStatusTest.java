package eu.pmant.app.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MeetingStatusTest {
    @Test
    void testWaitTranscribationTransitions() {
        MeetingStatus status = MeetingStatus.WAIT_TRANSCRIBATION;

        // Valid transition
        assertEquals(MeetingStatus.TRANSCRIBATION_IN_PROGRESS,
            status.transitTo(MeetingStatus.TRANSCRIBATION_IN_PROGRESS));

        // Invalid transitions
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.WAIT_TRANSCRIBATION));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_SUCCESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_FAIL));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_IN_PROGRESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_SUCCESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_FAIL));
    }

    @Test
    void testTranscribationInProgressTransitions() {
        MeetingStatus status = MeetingStatus.TRANSCRIBATION_IN_PROGRESS;

        // Valid transitions
        assertEquals(MeetingStatus.TRANSCRIBATION_SUCCESS,
            status.transitTo(MeetingStatus.TRANSCRIBATION_SUCCESS));
        assertEquals(MeetingStatus.TRANSCRIBATION_FAIL,
            status.transitTo(MeetingStatus.TRANSCRIBATION_FAIL));

        // Invalid transitions
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.WAIT_TRANSCRIBATION));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_IN_PROGRESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_IN_PROGRESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_SUCCESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_FAIL));
    }

    @Test
    void testTranscribationSuccessTransitions() {
        MeetingStatus status = MeetingStatus.TRANSCRIBATION_SUCCESS;

        // Valid transition
        assertEquals(MeetingStatus.MINUTES_OF_MEETING_IN_PROGRESS,
            status.transitTo(MeetingStatus.MINUTES_OF_MEETING_IN_PROGRESS));

        // Invalid transitions
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.WAIT_TRANSCRIBATION));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_IN_PROGRESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_SUCCESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_FAIL));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_SUCCESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_FAIL));
    }

    @Test
    void testTranscribationFailTransitions() {
        MeetingStatus status = MeetingStatus.TRANSCRIBATION_FAIL;

        // No valid transitions from this state
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.WAIT_TRANSCRIBATION));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_IN_PROGRESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_SUCCESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_FAIL));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_IN_PROGRESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_SUCCESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_FAIL));
    }

    // ... existing code ...

    @Test
    void testMinutesOfMeetingInProgressTransitions() {
        MeetingStatus status = MeetingStatus.MINUTES_OF_MEETING_IN_PROGRESS;

        // Valid transitions
        assertEquals(MeetingStatus.MINUTES_OF_MEETING_SUCCESS,
            status.transitTo(MeetingStatus.MINUTES_OF_MEETING_SUCCESS));
        assertEquals(MeetingStatus.MINUTES_OF_MEETING_FAIL,
            status.transitTo(MeetingStatus.MINUTES_OF_MEETING_FAIL));

        // Invalid transitions
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.WAIT_TRANSCRIBATION));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_IN_PROGRESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_SUCCESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_FAIL));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_IN_PROGRESS));
    }

    @Test
    void testMinutesOfMeetingSuccessTransitions() {
        MeetingStatus status = MeetingStatus.MINUTES_OF_MEETING_SUCCESS;

        // No valid transitions from this state
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.WAIT_TRANSCRIBATION));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_IN_PROGRESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_SUCCESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_FAIL));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_IN_PROGRESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_SUCCESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_FAIL));
    }

    @Test
    void testMinutesOfMeetingFailTransitions() {
        MeetingStatus status = MeetingStatus.MINUTES_OF_MEETING_FAIL;

        // No valid transitions from this state
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.WAIT_TRANSCRIBATION));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_IN_PROGRESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_SUCCESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.TRANSCRIBATION_FAIL));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_IN_PROGRESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_SUCCESS));
        assertThrows(RuntimeException.class,
            () -> status.transitTo(MeetingStatus.MINUTES_OF_MEETING_FAIL));
    }

    @Test
    void testGetAllowedStatusesForTransition() {
        // Test WAIT_TRANSCRIBATION allowed transitions
        assertEquals(1, MeetingStatus.WAIT_TRANSCRIBATION.getAllowedStatusesForTransition().size());
        assertTrue(MeetingStatus.WAIT_TRANSCRIBATION.getAllowedStatusesForTransition()
            .contains(MeetingStatus.TRANSCRIBATION_IN_PROGRESS));

        // Test TRANSCRIBATION_IN_PROGRESS allowed transitions
        assertEquals(2, MeetingStatus.TRANSCRIBATION_IN_PROGRESS.getAllowedStatusesForTransition().size());
        assertTrue(MeetingStatus.TRANSCRIBATION_IN_PROGRESS.getAllowedStatusesForTransition()
            .contains(MeetingStatus.TRANSCRIBATION_SUCCESS));
        assertTrue(MeetingStatus.TRANSCRIBATION_IN_PROGRESS.getAllowedStatusesForTransition()
            .contains(MeetingStatus.TRANSCRIBATION_FAIL));

        // Test TRANSCRIBATION_SUCCESS allowed transitions
        assertEquals(1, MeetingStatus.TRANSCRIBATION_SUCCESS.getAllowedStatusesForTransition().size());
        assertTrue(MeetingStatus.TRANSCRIBATION_SUCCESS.getAllowedStatusesForTransition()
            .contains(MeetingStatus.MINUTES_OF_MEETING_IN_PROGRESS));

        // Test terminal states have no allowed transitions
        assertTrue(MeetingStatus.TRANSCRIBATION_FAIL.getAllowedStatusesForTransition().isEmpty());
        assertTrue(MeetingStatus.MINUTES_OF_MEETING_IN_PROGRESS.getAllowedStatusesForTransition().size() == 2);
        assertTrue(MeetingStatus.MINUTES_OF_MEETING_IN_PROGRESS.getAllowedStatusesForTransition()
            .contains(MeetingStatus.MINUTES_OF_MEETING_SUCCESS));
        assertTrue(MeetingStatus.MINUTES_OF_MEETING_IN_PROGRESS.getAllowedStatusesForTransition()
            .contains(MeetingStatus.MINUTES_OF_MEETING_FAIL));
        assertTrue(MeetingStatus.MINUTES_OF_MEETING_SUCCESS.getAllowedStatusesForTransition().isEmpty());
        assertTrue(MeetingStatus.MINUTES_OF_MEETING_FAIL.getAllowedStatusesForTransition().isEmpty());
    }
}