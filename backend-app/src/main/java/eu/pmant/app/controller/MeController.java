package eu.pmant.app.controller;

import eu.pmant.app.dto.SessionData;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeController {

    private final HttpSession session;

    @GetMapping("/api/me")
    public ResponseEntity<?> me() {
        SessionData sessionData = (SessionData) session.getAttribute("sessionData");
        return ResponseEntity.ok(SessionData.builder()
                .login(sessionData.getLogin())
                .userId(sessionData.getUserId())
                .build()
        );
    }
}
