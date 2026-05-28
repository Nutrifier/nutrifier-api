package fi.nutrifier.unit.controller;

import fi.nutrifier.controllers.GoalsController;
import fi.nutrifier.dto.GoalsUpdateRequest;
import fi.nutrifier.services.*;
import fi.nutrifier.unit.utils.TestObjects;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GoalsController.class)
@ActiveProfiles("test")
public class GoalsControllerTest extends ControllerTestInterface<GoalsService> {

    protected GoalsControllerTest() {
        super("/api/v1/goals");
    }

    @Test
    @WithMockUser
    public void testGetUsersGoals_ReturnOk() throws Exception {
        when(service.getUserGoals(any(UUID.class)))
                .thenReturn(new ResponseEntity<>(TestObjects.goals.toResponse(), HttpStatus.OK));

        mockMvc.perform(get(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.goalType").value("MAINTAIN"))
            .andExpect(jsonPath("$.targetWeight").value(60.0))
            .andExpect(jsonPath("$.isReached").value(false));

        verify(service).getUserGoals(any(UUID.class));
    }

    @Test
    public void testGetUsersGoals_WithoutAuth_ReturnUnauthorized() throws Exception {
        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(service);
    }

    @Test
    @WithMockUser
    public void testUpdateUsersGoals_ReturnOk() throws Exception {
        TestObjects.goals.setTargetWeight(140.0);

        when(service.update(any(UUID.class), any(GoalsUpdateRequest.class)))
                .thenReturn(new ResponseEntity<>(TestObjects.goals.toResponse(), HttpStatus.OK));

        mockMvc.perform(patch(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.goals))
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goalType").value("MAINTAIN"))
                .andExpect(jsonPath("$.targetWeight").value(140.0))
                .andExpect(jsonPath("$.isReached").value(false));

        verify(service).update(any(UUID.class), any(GoalsUpdateRequest.class));
    }

    @Test
    public void testUpdateUsersGoals_WithoutAuth_ReturnUnauthorized() throws Exception {
        mockMvc.perform(patch(baseUrl)
                        .with(csrf()) // MockMvc expects csrf is in use
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.goals)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(service);
    }

    @Test
    @WithMockUser
    public void testCalculate_ReturnOk() throws Exception {
        when(service.recalculateGoals(any(UUID.class)))
                .thenReturn(new ResponseEntity<>(TestObjects.goals.toResponse(), HttpStatus.OK));

        mockMvc.perform(post(baseUrl + "/recalculate")
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goalType").value("MAINTAIN"))
                .andExpect(jsonPath("$.targetWeight").value(60.0))
                .andExpect(jsonPath("$.isReached").value(false));

        verify(service).recalculateGoals(any(UUID.class));
    }

    @Test
    public void testCalculate_WithoutAuth_ReturnOk() throws Exception {
        mockMvc.perform(post(baseUrl + "/recalculate")
                        .with(csrf()) // MockMvc expects csrf is in use
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.goals)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(service);
    }
}
