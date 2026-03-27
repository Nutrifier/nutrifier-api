package fi.nutrifier.unit.controller;

import fi.nutrifier.controllers.SettingsController;
import fi.nutrifier.dto.SettingsUpdateRequest;
import fi.nutrifier.services.*;
import fi.nutrifier.unit.utils.TestObjects;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SettingsController.class)
@ActiveProfiles("test")
public class SettingsControllerTest extends ControllerTestInterface<SettingsService> {

    protected SettingsControllerTest() {
        super("/api/settings");
    }

    @Test
    public void testGetUsersSettings_ReturnOk() throws Exception {
        when(service.get(eq(TestObjects.id1)))
                .thenReturn(ResponseEntity.ok(TestObjects.settings));

        mockMvc.perform(get(baseUrl)
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.language").value("EN"))
            .andExpect(jsonPath("$.weightUnit").value("G"))
            .andExpect(jsonPath("$.weighInReminderEnabled").value(true));

        verify(service).get(any(UUID.class));
    }

    @Test
    public void testGetUsersSettings_WithoutAuth_ReturnUnauthorized() throws Exception {
        mockMvc.perform(get(baseUrl)
                        .with(csrf())) // MockMvc expects csrf is in use
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(service);
    }

    @Test
    public void testUpdateUsersSettings_ReturnOk() throws Exception {
        TestObjects.settings.setLanguage("SE");

        when(service.update(eq(TestObjects.id1), any(SettingsUpdateRequest.class)))
                .thenReturn(ResponseEntity.ok(TestObjects.settings));

        mockMvc.perform(patch(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.settings))
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.language").value("SE"))
            .andExpect(jsonPath("$.weightUnit").value("G"))
            .andExpect(jsonPath("$.weighInReminderEnabled").value(true));

        verify(service).update(any(UUID.class), any(SettingsUpdateRequest.class));
    }

    @Test
    public void testUpdateSettings_WithoutAuth_ReturnUnauthorized() throws Exception {
        mockMvc.perform(patch(baseUrl)
                        .with(csrf()) // MockMvc expects csrf is in use
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.settings)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(service);
    }
}
