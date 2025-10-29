package eu.pmant.app.controller;

import eu.pmant.app.dto.RegistrationRequest;
import eu.pmant.app.dto.RegistrationResponse;
import eu.pmant.app.generated.jooq.tables.pojos.UserAccount;
import eu.pmant.app.service.UserService;
import eu.pmant.app.session.SessionDataProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class RegistrationController {

    private final UserService userService;
    private final SessionDataProvider sessionDataProvider;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest request, HttpServletRequest httpServletRequest) {
        Optional<UserAccount> registeredUser = userService.registerUser(request.getLogin(), request.getPassword());

        if (registeredUser.isPresent()) {
            sessionDataProvider.createSession(httpServletRequest);
            return ResponseEntity.ok(new RegistrationResponse("User registered successfully.", registeredUser.get().getId()));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new RegistrationResponse("Login already exists.", null));
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