package eu.pmant.app.controller;

import eu.pmant.app.dto.LoginRequest;
import eu.pmant.app.dto.LoginResponse;
import eu.pmant.app.dto.LogoutResponse;
import eu.pmant.app.dto.SessionData;
import eu.pmant.app.model.User;
import eu.pmant.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        Optional<User> userOptional = userService.findByLogin(request.getLogin());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }

        User user = userOptional.get();
        if (!userService.checkPassword(request.getPassword(), user.getPasswordHash())) {
            log.info("Password is wrong");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }

        if (httpRequest.getSession(false) != null) {
            return ResponseEntity.internalServerError()
                    .body(LoginResponse.builder()
                            .success(false)
                            .message("You already in session")
                            .build()
                    );
        }

        // Create session and store user information
        HttpSession session = httpRequest.getSession(true);
        SessionData sessionData = SessionData.builder()
                .userId(user.getId())
                .login(user.getLogin())
                .build();
        session.setAttribute("sessionData", sessionData);
        return ResponseEntity.ok(LoginResponse.builder().success(true)
                .success(true)
                .message(String.format("Logged in ok (userId=%d)", sessionData.getUserId()))
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            log.info("Session not null, invalidating...");
            session.invalidate();
            return ResponseEntity.ok(
                    LogoutResponse.builder()
                            .success(true)
                            .message("Logged out")
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    LogoutResponse.builder()
                            .success(false)
                            .message("You are not logged in")
                            .build()
            );
        }

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new TreeMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
