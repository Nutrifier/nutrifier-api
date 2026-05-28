package fi.nutrifier.unit.controller;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.AnalyticsController;
import fi.nutrifier.controllers.FoodEntryController;
import fi.nutrifier.dto.FoodEntryRequest;
import fi.nutrifier.dto.FoodEntryResponse;
import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.enums.AnalyticsTimePeriod;
import fi.nutrifier.enums.MealType;
import fi.nutrifier.services.AnalyticsService;
import fi.nutrifier.services.FoodEntryService;
import fi.nutrifier.unit.utils.TestObjects;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticsController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class AnalyticsControllerTest extends ControllerTestInterface<AnalyticsService> {

    protected AnalyticsControllerTest() {
        super("/api/v1/analytics");
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "USER"
    )
    public void testGetAnalytics_ReturnOk() throws Exception {
        when(service.calculateAnalyticsWithinDateRange(any(LocalDate.class), any(UUID.class), any(AnalyticsTimePeriod.class)))
                .thenReturn(ResponseEntity.ok(TestObjects.analyticsFull.toResponse()));

        mockMvc.perform(get(baseUrl)
                        .param("date", "2026-03-26")
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.successfulDayCount", CoreMatchers.is(TestObjects.analyticsFull.getSuccessfulDayCount())));

        verify(service).calculateAnalyticsWithinDateRange(any(LocalDate.class), any(UUID.class), any(AnalyticsTimePeriod.class));
    }

    @Test
    void testGetAnalytics_WithoutAuth_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl)
                        .with(csrf()) // MockMvc expects csrf is in use
                        .param("date", "2026-03-26"))
                .andExpect(status().isForbidden());

        verifyNoMoreInteractions(service);
    }
}
