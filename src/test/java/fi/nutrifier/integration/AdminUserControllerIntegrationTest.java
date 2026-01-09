package fi.nutrifier.integration;

import fi.nutrifier.dto.UserDto;
import fi.nutrifier.entities.Role;
import fi.nutrifier.entities.User;
import fi.nutrifier.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.nutrifier.unit.utils.TestObjects;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminUserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto testUser;
    private final String baseUrl = "/api/admin/users";

    @BeforeEach
    public void setup() {
        UserDto userDto = new UserDto();
        userDto.initialize("test@gmail.com", Role.ROLE_USER);
        testUser = userDto;
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAll_asAdmin_ReturnEmptyList() throws Exception {
        mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));
    }
}