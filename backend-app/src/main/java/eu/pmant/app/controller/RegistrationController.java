package eu.pmant.app.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest request) {
        Optional<User> registeredUser = userService.registerUser(request.getLogin(), request.getPassword());

        if (registeredUser.isPresent()) {
            return ResponseEntity.ok(new RegistrationResponse("User registered successfully.", registeredUser.get().getId()));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new RegistrationResponse("Login already exists.", null));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put("error", errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}