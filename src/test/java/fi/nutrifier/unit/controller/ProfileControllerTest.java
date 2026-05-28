package fi.nutrifier.unit.controller;

import fi.nutrifier.controllers.ProfileController;
import fi.nutrifier.dto.MealRequest;
import fi.nutrifier.dto.ProfileUpdateRequest;
import fi.nutrifier.services.ProfileService;
import fi.nutrifier.unit.utils.TestObjects;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
@ActiveProfiles("test")
public class ProfileControllerTest extends ControllerTestInterface<ProfileService> {

    protected ProfileControllerTest() {
        super("/api/v1/profile");
    }

    @Test
    public void testGetById_ReturnOk() throws Exception {
        when(service.getProfile(any(UUID.class)))
                .thenReturn(new ResponseEntity<>(TestObjects.profile.toResponse(), HttpStatus.OK));

        mockMvc.perform(get(baseUrl)
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.height").value(180.0));

        verify(service).getProfile(any(UUID.class));
    }

    @Test
    public void testGetById_WithoutAuth_ReturnUnauthorized() throws Exception {
        mockMvc.perform(get(baseUrl)
                        .with(csrf())) // MockMvc expects csrf is in use
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(service);
    }

    @Test
    public void testUpdate_ReturnOk() throws Exception {
        when(service.update(any(UUID.class), any(ProfileUpdateRequest.class)))
                .thenReturn(new ResponseEntity<>(TestObjects.profile.toResponse(), HttpStatus.OK));

        mockMvc.perform(patch(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.profile))
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.height").value(180.0));

        verify(service).update(any(UUID.class), any(ProfileUpdateRequest.class));
    }

    @Test
    public void testUpdate_WithoutAuth_ReturnUnauthorized() throws Exception {
        mockMvc.perform(patch(baseUrl)
                        .with(csrf()) // MockMvc expects csrf is in use
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.profile)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(service);
    }
}
