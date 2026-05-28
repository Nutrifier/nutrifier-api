package fi.nutrifier.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.nutrifier.dto.RegisterRequest;
import fi.nutrifier.enums.ActivityLevel;
import fi.nutrifier.enums.GoalType;
import fi.nutrifier.enums.Sex;
import fi.nutrifier.repositories.UserRepository;
import fi.nutrifier.unit.utils.TestObjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        TestObjects.reset();
    }

    @Test
    @WithMockUser
    void registrationCreatesDefaultUserSettings() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(
                "test@gmail.com",
                "qwerty",
                Sex.FEMALE,
                20,
                170,
                ActivityLevel.SEDENTARY,
                GoalType.MAINTAIN,
                50.0,
                50.0,
                LocalDate.now().plusYears(1)
        );

        // Register user
        String responseJson = mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        JsonNode json = objectMapper.readTree(responseJson);
        String token = json.get("token").asText();
        String userId = json.get("userId").asText();

        // Fetch user data
        mockMvc.perform(get("/api/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(userId));
    }
}