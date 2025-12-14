package eu.pmant.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.pmant.app.config.TestConfig;
import eu.pmant.app.dto.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class RegistrationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

}