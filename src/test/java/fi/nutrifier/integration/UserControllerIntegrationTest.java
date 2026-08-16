package fi.nutrifier.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.nutrifier.dto.RegisterRequest;
import fi.nutrifier.entities.ActivityLevel;
import fi.nutrifier.entities.Role;
import fi.nutrifier.enums.GoalType;
import fi.nutrifier.enums.Sex;
import fi.nutrifier.repositories.UserRepository;
import fi.nutrifier.repositories.reference.ActivityLevelRepository;
import fi.nutrifier.repositories.reference.DietRepository;
import fi.nutrifier.repositories.reference.RoleRepository;
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
import java.util.UUID;

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
    private DietRepository dietRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ActivityLevelRepository activityLevelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        TestObjects.reset();
    }

    @Test
    void registrationCreatesDefaultUserSettings() throws Exception {
        roleRepository.save(TestObjects.ROLE_REGULAR);
        dietRepository.save(TestObjects.DIET_STANDARD);
        ActivityLevel savedActivityLevel = activityLevelRepository.save(TestObjects.ACTIVITY_LEVEL_SEDENTARY);

        RegisterRequest registerRequest = new RegisterRequest(
                "test@gmail.com",
                "Qwerty123!",
                Sex.FEMALE,
                20,
                170,
                savedActivityLevel,
                GoalType.MAINTAIN,
                50.0,
                50.0,
                LocalDate.now().plusYears(1)
        );

        // Register user
        String responseJson = mockMvc.perform(post("/api/v1/register")
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
        mockMvc.perform(get("/api/v1/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(userId));
    }
}