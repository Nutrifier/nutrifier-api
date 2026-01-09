package fi.nutrifier.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.nutrifier.dto.AuthRequest;
import fi.nutrifier.dto.UserDto;
import fi.nutrifier.entities.Role;
import fi.nutrifier.repositories.UserRepository;
import fi.nutrifier.unit.utils.TestObjects;
import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
    void registeration_creates_default_user_settings() throws Exception {
        AuthRequest authRequest = new AuthRequest("test@gmail.com", "qwerty");

        // Register user
        String responseJson = mockMvc.perform(post("/api/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        String userId = objectMapper.readTree(responseJson).get("userId").asText();

        // Fetch user data
        mockMvc.perform(get("/api/users/" + userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.settings").exists());
    }
}