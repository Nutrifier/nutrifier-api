package fi.nutrifier.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.nutrifier.controllers.UserController;
import fi.nutrifier.controllers.UserSettingsController;
import fi.nutrifier.services.*;
import fi.nutrifier.unit.utils.TestObjects;
import fi.nutrifier.utils.JwtTokenUtil;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserSettingsController.class)
@ActiveProfiles("test")
public class UserSettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserSettingsService userSettingsService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setup() {
        TestObjects.reset();
    }

    @Test
    public void testGetUsersSettings_ReturnOk() throws Exception {
        TestObjects.user1.setSettings(TestObjects.settings);

        when(userSettingsService.get(eq(TestObjects.userId1)))
                .thenReturn(ResponseEntity.ok(TestObjects.settings));

        mockMvc.perform(get("/api/users/settings")
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.userId1))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.language").value("EN"))
            .andExpect(jsonPath("$.weightUnit").value("G"))
            .andExpect(jsonPath("$.weighInReminderEnabled").value(true));
    }

    @Test
    public void testUpdateUsersSettings_ReturnOk() throws Exception {
        TestObjects.settings.setLanguage("SE");
        TestObjects.user1.setSettings(TestObjects.settings);

        when(userSettingsService.update(eq(TestObjects.userId1), any()))
                .thenReturn(ResponseEntity.ok(TestObjects.settings));

        mockMvc.perform(patch("/api/users/settings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.settings))
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.userId1))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.language").value("SE"))
            .andExpect(jsonPath("$.weightUnit").value("G"))
            .andExpect(jsonPath("$.weighInReminderEnabled").value(true));
    }
}
