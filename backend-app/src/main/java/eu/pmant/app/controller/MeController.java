package eu.pmant.app.controller;

import eu.pmant.app.dto.MeData;
import eu.pmant.app.dto.MeResponse;
import eu.pmant.app.dto.SessionData;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeController {

    private final HttpSession session;

    @PostMapping("/api/me")
    public ResponseEntity<?> me() {
        SessionData sessionData = (SessionData) session.getAttribute("sessionData");
        return ResponseEntity.ok(
            MeResponse.builder()
                .success(true)
                .data(
                    MeData.builder()
                        .username(sessionData.getLogin())
                        .userId(sessionData.getUserId())
                        .build()
                )
                .build()
        );
    }
}
