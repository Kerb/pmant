package eu.pmant.app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class PingControllerTest {

    private PingController pingController;

    @BeforeEach
    void setUp() {
        pingController = new PingController();
    }

    @Test
    void testPing() {
        String expected = "pong";
        ResponseEntity<String> actual = pingController.ping();
        assertEquals(expected, actual.getBody(), "Ping method should return 'pong'");
    }

    @Test
    void testHealth() {
        ResponseEntity<String> response = pingController.ping();
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Health check should return OK status");
        assertEquals("pong", response.getBody(), "Health check should return expected message");
    }
}