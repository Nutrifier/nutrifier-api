package fi.nutrifier.unit.controller.admin;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.admin.AdminRecipeController;
import fi.nutrifier.dto.*;
import fi.nutrifier.enums.ReportStatus;
import fi.nutrifier.services.RecipeService;
import fi.nutrifier.unit.controller.ControllerTestInterface;
import fi.nutrifier.unit.utils.TestObjects;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminRecipeController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class AdminRecipeControllerTest extends ControllerTestInterface<RecipeService> {

    protected AdminRecipeControllerTest() {
        super("/api/v1/admin/recipes");
    }

    private Page<RecipeResponse> mockPage() {
        List<RecipeResponse> list = List.of(TestObjects.recipe.toResponse());
        return new PageImpl<>(list, PageRequest.of(0, 10), 1);
    }

    private Page<RecipeReportResponse> mockPageReport() {
        List<RecipeReportResponse> list = List.of(TestObjects.recipeReport.toResponse());
        return new PageImpl<>(list, PageRequest.of(0, 10), 1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAll_AsAdmin_ReturnRecipes() throws Exception {
        when(service.getAll(any(Integer.class), any(Integer.class)))
                .thenReturn(new ResponseEntity<>(mockPage(), HttpStatus.OK));

        mockMvc.perform(get(baseUrl)
                .param("userId", TestObjects.id1.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()", CoreMatchers.is(1)))
            .andExpect(jsonPath("$.content[0].name", CoreMatchers.is("My recipe")));

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

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "ADMIN"
    )
    void testGetAllReports_ReturnAllReports() throws Exception {
        when(service.getAllReports(any(Integer.class), any(Integer.class)))
                .thenReturn(new ResponseEntity<>(mockPageReport(), HttpStatus.OK));

        // Act and Assert
        mockMvc.perform(get(baseUrl + "/report")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", CoreMatchers.is(1)))
                .andExpect(jsonPath("$.content[0].reason", CoreMatchers.is("Wrong values")))
                .andExpect(jsonPath("$.content[0].status", CoreMatchers.is(ReportStatus.PENDING.toString())));

        verify(service).getAllReports(anyInt(), anyInt());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllReports_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl + "/report")).andExpect(status().isForbidden());
    }

    @Test
    void testGetAllReports_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl + "/report")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "ADMIN"
    )
    void testReviewReport_ReturnAllReports() throws Exception {
        when(service.reviewReport(any(UUID.class), any(UUID.class), any(RecipeReportReviewRequest.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(patch(baseUrl + "/report/{id}", TestObjects.foodReport1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.foodReport2)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testReviewReport_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(patch(baseUrl + "/report/{id}", 99)).andExpect(status().isForbidden());
    }

    @Test
    void testReviewReport_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(patch(baseUrl + "/report/{id}", 99)).andExpect(status().isForbidden());
    }
}
