package fi.nutrifier.unit.controller.admin;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.admin.AdminFoodEntryController;
import fi.nutrifier.dto.FoodEntryResponse;
import fi.nutrifier.services.FoodEntryService;
import fi.nutrifier.unit.controller.ControllerTestInterface;
import fi.nutrifier.unit.utils.TestObjects;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminFoodEntryController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class AdminFoodEntryControllerTest extends ControllerTestInterface<FoodEntryService> {

    protected AdminFoodEntryControllerTest() {
        super("/api/v1/admin/food-entries");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllByUserId_AsAdmin_ReturnLogs() throws Exception {
        List<FoodEntryResponse> foodEntries = List.of(TestObjects.foodEntry1.toResponse(), TestObjects.foodEntry2.toResponse());

        Pageable pageable = PageRequest.of(1, 10);
        Page<FoodEntryResponse> mockPage = new PageImpl<>(foodEntries, pageable, foodEntries.size());

        // Mock service layer
        when(service.getAllByUserId(any(UUID.class), any(Integer.class), any(Integer.class)))
                .thenReturn(new ResponseEntity<>(mockPage, HttpStatus.OK));

        mockMvc.perform(get(baseUrl)
                .param("userId", TestObjects.id1.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()", CoreMatchers.is(2)))
            .andExpect(jsonPath("$.content[0].mealType", CoreMatchers.is("BREAKFAST")))
            .andExpect(jsonPath("$.content[1].mealType", CoreMatchers.is("LUNCH")));

        verify(service, times(1)).getAllByUserId(any(UUID.class), anyInt(), anyInt());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAll_AsUser_ReturnUnauthorized() throws Exception {
        mockMvc.perform(get(baseUrl)).andExpect(status().isForbidden());
    }

    @Test
    public void testGetAll_AsNobody_ReturnUnauthorized() throws Exception {
        mockMvc.perform(get(baseUrl)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetById_AsAdmin_ReturnUnauthorized() throws Exception {
        when(service.getById(TestObjects.id)).thenReturn(new ResponseEntity<>(TestObjects.foodEntry1.toResponse(), HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/{id}", TestObjects.id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", CoreMatchers.is(TestObjects.foodEntry1.getAmount())))
                .andExpect(jsonPath("$.mealType", CoreMatchers.is(TestObjects.foodEntry1.getMealType().toString())));

        verify(service, times(1)).getById(TestObjects.id);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetById_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}", 1)).andExpect(status().isForbidden());
    }

    @Test
    public void testGetById_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}", 1)).andExpect(status().isForbidden());
    }
}
