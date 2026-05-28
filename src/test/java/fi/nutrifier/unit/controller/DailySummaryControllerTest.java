package fi.nutrifier.unit.controller;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.DailySummaryController;
import fi.nutrifier.dto.ApiResponse;
import fi.nutrifier.services.DailySummaryService;
import fi.nutrifier.unit.utils.TestObjects;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailySummaryController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class DailySummaryControllerTest extends ControllerTestInterface<DailySummaryService> {

    protected DailySummaryControllerTest() {
        super("/api/daily-nutrition-summary");
    }

    private final String baseUrl = "/api/daily-nutrition-summary";

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "USER"
    )
    public void testCreate_ReturnCreated() throws Exception {
        when(service.create(any(UUID.class), any(DailyNutritionSummaryCreateRequest.class)))
                .thenReturn(new ApiResponse<>(TestObjects.dailySummary.toResponse(), HttpStatus.CREATED));

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.dailySummary.toCreateRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.caloriesTarget", CoreMatchers.is(120.0)));

        verify(service).create(any(UUID.class), any(DailyNutritionSummaryCreateRequest.class));
    }

    @Test
    public void testCreate_AsNobody_ReturnUnauthorized() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .with(csrf()) // MockMvc expects csrf is in use
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.dailySummary.toCreateRequest())))
                .andExpect(status().isForbidden()); // TODO: Why doesn't this accept UNAUTHORIZED

        verifyNoInteractions(service);
    }

    @Test
    @WithMockUser
    public void testUpdate_ReturnUpdated() throws Exception {
        when(service.update(any(UUID.class), any(UUID.class), any(DailyNutritionSummaryUpdateRequest.class)))
                .thenReturn(new ResponseEntity<>(TestObjects.dailySummary.toResponse(), HttpStatus.OK));

        mockMvc.perform(patch(baseUrl + "/{id}", TestObjects.dailySummary.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.dailySummary.toUpdateRequest()))
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.caloriesTarget", CoreMatchers.is(120.0)));

        verify(service).update(any(UUID.class), any(UUID.class), any(DailyNutritionSummaryUpdateRequest.class));
    }

    @Test
    public void testUpdate_AsNobody_ReturnUnauthorized() throws Exception {
        mockMvc.perform(patch(baseUrl + "/{id}", TestObjects.dailySummary.getId().toString())
                        .with(csrf()) // MockMvc expects csrf is in use
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.dailySummary.toUpdateRequest())))
                .andExpect(status().isForbidden()); // TODO: Why doesn't this accept UNAUTHORIZED

        verifyNoInteractions(service);
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "USER"
    )
    public void testGetByDate_ReturnDailyNutritionSummaries() throws Exception {
        when(service.getByDateAndUser(any(LocalDate.class), any(UUID.class)))
                .thenReturn(new ApiResponse<>(TestObjects.dailySummary.toResponse(), HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/by-date")
                        .param("date", "2026-03-26"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.caloriesTarget", CoreMatchers.is(120.0)));

        verify(service).getByDateAndUser(any(LocalDate.class), any(UUID.class));
    }

    @Test
    public void testGetByDate_AsNobody_ReturnUnauthorized() throws Exception {
        mockMvc.perform(get(baseUrl + "/by-date")
                        .with(csrf()) // MockMvc expects csrf is in use
                        .param("date", "2026-03-26"))
                .andExpect(status().isForbidden()); // TODO: Why doesn't this accept UNAUTHORIZED

        verifyNoInteractions(service);
    }
}
