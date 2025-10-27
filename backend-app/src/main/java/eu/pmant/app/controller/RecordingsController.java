package eu.pmant.app.controller;

import eu.pmant.app.dto.Recording;
import eu.pmant.app.dto.RecordingsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecordingsController {

    @PostMapping("/recordings")
    public ResponseEntity<RecordingsResponse> recordings() {
        RecordingsResponse response = RecordingsResponse.builder()
            .success(true)
            .recordings(
                List.of(
                    Recording.builder()
                        .id(1L)
                        .title("Product Strategy Meeting")
                        .date("2025-01-20")
                        .time("10:00 AM")
                        .duration("45 min")
                        .participants(8)
                        .status("Completed")
                        .build(),
                    Recording.builder()
                        .id(2L)
                        .title("Design Review")
                        .date("2025-01-19")
                        .time("2:30 PM")
                        .duration("30 min")
                        .participants(5)
                        .status("Completed")
                        .build(),
                    Recording.builder()
                        .id(3L)
                        .title("Client Call - Q1 Planning")
                        .date("2025-01-18")
                        .time("11:00 AM")
                        .duration("60 min")
                        .participants(12)
                        .status("Completed")
                        .build(),
                    Recording.builder()
                        .id(4L)
                        .title("Team Standup")
                        .date("2025-01-18")
                        .time("9:00 AM")
                        .duration("15 min")
                        .participants(10)
                        .status("Completed")
                        .build()
                )
            ).build();
        return ResponseEntity.ok(response);
    }
}
