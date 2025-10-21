package eu.pmant.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.pmant.app.dto.RegistrationRequest;
import eu.pmant.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegistrationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll(); // Clear database before each test
    }

    @Test
    void registerUser_success() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setLogin("testuser");
        request.setPassword("password123");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully."))
                .andExpect(jsonPath("$.userId").exists());
    }

    @Test
    void registerUser_loginAlreadyExists() throws Exception {
        // First, register a user successfully
        RegistrationRequest firstRequest = new RegistrationRequest();
        firstRequest.setLogin("existinguser");
        firstRequest.setPassword("password123");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstRequest)))
                .andExpect(status().isOk());

        // Then, try to register with the same login
        RegistrationRequest secondRequest = new RegistrationRequest();
        secondRequest.setLogin("existinguser");
        secondRequest.setPassword("anotherpassword");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Login already exists."));
    }

    @Test
    void registerUser_passwordTooShort() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setLogin("shortpassuser");
        request.setPassword("short"); // Less than 8 characters

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").value("Password must be at least 8 characters long"));
    }

    @Test
    void registerUser_emptyLogin() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setLogin(""); // Empty login
        request.setPassword("password123");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.login").value("Login cannot be empty"));
    }

    @Test
    void registerUser_emptyPassword() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setLogin("emptypassuser");
        request.setPassword(""); // Empty password

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").value("Password must be at least 8 characters long"));
    }
}