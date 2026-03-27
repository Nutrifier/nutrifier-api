package fi.nutrifier.unit.controller;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.FoodEntryController;
import fi.nutrifier.dto.FoodEntryRequest;
import fi.nutrifier.dto.FoodEntryResponse;
import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.services.FoodEntryService;
import fi.nutrifier.services.UserService;
import fi.nutrifier.unit.utils.TestObjects;
import fi.nutrifier.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FoodEntryController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class FoodEntryControllerTest extends ControllerTestInterface<FoodEntryService> {

    protected FoodEntryControllerTest() {
        super("/api/food-entries");
    }

    @Test
    @WithMockUser
    public void testCreateLog_ReturnCreated() throws Exception {
        when(service.create(any(UUID.class), any(FoodEntryRequest.class)))
                .thenReturn(new ResponseEntity<>(TestObjects.foodEntry1.toResponse(), HttpStatus.CREATED));

        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.foodEntry1))
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.amount", CoreMatchers.is(TestObjects.foodEntry1.getAmount())))
            .andExpect(jsonPath("$.mealType", CoreMatchers.is(TestObjects.foodEntry1.getMealType())));

        verify(service).create(any(UUID.class), any(FoodEntryRequest.class));
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "USER"
    )
    public void testCreateLog_InvalidAmount_ReturnBadRequest() throws Exception {
        TestObjects.foodEntry1.setAmount(-1.0);

        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.foodEntry1)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    public void testCreateLog_WithoutAuth_ReturnForbidden() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.foodEntry1)))
                .andExpect(status().isForbidden());

        verifyNoMoreInteractions(service);
    }

    @Test
    @WithMockUser
    public void testUpdateLog_ReturnFood() throws Exception {
        TestObjects.foodEntry1.setAmount(100.0);
        TestObjects.foodEntry1.setMealType("SNACKS");

        // Use eq(1L) to match the exact ID and any(Log.class) to allow any User instance.
        when(service.update(eq(TestObjects.id1), eq(TestObjects.id), any(FoodEntry.class)))
                .thenReturn(new ResponseEntity<>(TestObjects.foodEntry1.toResponse(), HttpStatus.OK));

        mockMvc.perform(patch(baseUrl + "/{id}", TestObjects.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.foodEntry1))
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.amount", CoreMatchers.is(100.0)))
            .andExpect(jsonPath("$.mealType", CoreMatchers.is("SNACKS")));

        verify(service).update(eq(TestObjects.id1), eq(TestObjects.id), any(FoodEntry.class));
    }

    @Test
    public void testUpdateLog_WithoutAuth_ReturnForbidden() throws Exception {
        mockMvc.perform(patch(baseUrl + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.foodEntry1)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(service);
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "USER"
    )
    public void testDeleteLog_ReturnEmpty() throws Exception {
        when(service.delete(TestObjects.id1, TestObjects.id)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        MvcResult result = mockMvc.perform(delete(baseUrl + "/{id}", TestObjects.id.toString()))
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().isEmpty());

        verify(service).delete(any(UUID.class), any(UUID.class));
    }

    @Test
    public void testDeleteLog_WithoutAuth_ReturnForbidden() throws Exception {
        mockMvc.perform(delete(baseUrl +"/{id}", 1)).andExpect(status().isForbidden());

        verifyNoInteractions(service);
    }

    @Test
    @WithMockUser
    public void testGetByDate_ReturnLogs() throws Exception {
        List<FoodEntryResponse> foodEntries = new ArrayList<>();
        foodEntries.add(TestObjects.foodEntry1.toResponse());
        foodEntries.add(TestObjects.foodEntry2.toResponse());

        when(service.getLogsByDateAndUser(TestObjects.date, TestObjects.id1))
                .thenReturn(new ResponseEntity<>(foodEntries, HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/by-date")
                .param("date", TestObjects.date.toString())
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", CoreMatchers.is(2)));

        verify(service).getLogsByDateAndUser(any(LocalDate.class), any(UUID.class));
    }

    @Test
    public void testGetByDate_WithoutAuth_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl + "/by-date")
                        .param("date", TestObjects.date.toString()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(service);
    }
}
