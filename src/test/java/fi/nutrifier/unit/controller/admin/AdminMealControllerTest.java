package fi.nutrifier.unit.controller.admin;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.admin.AdminMealController;
import fi.nutrifier.dto.MealResponse;
import fi.nutrifier.services.MealService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminMealController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class AdminMealControllerTest extends ControllerTestInterface<MealService> {

    protected AdminMealControllerTest() {
        super("/api/admin/meals");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAll_AsAdmin_ReturnMeals() throws Exception {
        List<MealResponse> meals = List.of(TestObjects.meal.toResponse());

        Pageable pageable = PageRequest.of(1, 10);
        Page<MealResponse> mockPage = new PageImpl<>(meals, pageable, meals.size());

        // Mock service layer
        when(service.getAll(any(Integer.class), any(Integer.class)))
                .thenReturn(new ResponseEntity<>(mockPage, HttpStatus.OK));

        mockMvc.perform(get(baseUrl)
                .param("userId", TestObjects.id1.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()", CoreMatchers.is(1)))
            .andExpect(jsonPath("$.content[0].name", CoreMatchers.is("My meal")));

        verify(service).getAll(anyInt(), anyInt());
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
}
