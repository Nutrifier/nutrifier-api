package fi.nutrifier.unit.controller.admin;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.admin.AdminFoodController;
import fi.nutrifier.dto.FoodReportResponse;
import fi.nutrifier.dto.FoodReportReviewRequest;
import fi.nutrifier.dto.FoodRequest;
import fi.nutrifier.enums.ReportStatus;
import fi.nutrifier.services.FoodService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminFoodController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
class AdminFoodControllerTest extends ControllerTestInterface<FoodService> {

    protected AdminFoodControllerTest() {
        super("/api/v1/admin/foods");
    }

    @Test
    void testGetAll_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "ADMIN"
    )
    void testUpdateFood_AllFields_ReturnFood() throws Exception {
        TestObjects.food1.setName("New name");
        TestObjects.food1.setCalories(150.0);
        TestObjects.food1.setBarcode("1536473434");
        TestObjects.food1.setServingSize(400);
        TestObjects.food1.setCarbs(128.0);
        TestObjects.food1.setProtein(24.0);
        TestObjects.food1.setFat(45.0);

        // Use eq(1L) to match the exact ID and any(Food.class) to allow any User instance.
        when(service.getByIds(List.of(TestObjects.id)))
                .thenReturn(new ResponseEntity<>(List.of(TestObjects.food1.toResponse()), HttpStatus.OK));
        when(service.update(eq(TestObjects.id), any(UUID.class), any(FoodRequest.class))).
                thenReturn(new ResponseEntity<>(TestObjects.food1.toResponse(), HttpStatus.OK));

        mockMvc.perform(patch(baseUrl + "/{id}", TestObjects.id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.food1.toRequest())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is("New name")))
                .andExpect(jsonPath("$.calories", CoreMatchers.is(150.0)))
                .andExpect(jsonPath("$.barcode", CoreMatchers.is("1536473434")))
                .andExpect(jsonPath("$.servingSize", CoreMatchers.is(400)))
                .andExpect(jsonPath("$.carbs", CoreMatchers.is(128.0)))
                .andExpect(jsonPath("$.protein", CoreMatchers.is(24.0)))
                .andExpect(jsonPath("$.fat", CoreMatchers.is(45.0)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateFood_InvalidObject_ReturnsBadRequest() throws Exception {
        TestObjects.food1.setCalories(-100.0);

        mockMvc.perform(patch(baseUrl + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.food1.toRequest())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "ADMIN"
    )
    void updateFood_WhenServiceReturnsNotFound_Returns404() throws Exception {
        when(service.update(eq(TestObjects.id), any(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        mockMvc.perform(patch(baseUrl + "/{id}", TestObjects.id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.food1.toRequest())))
                .andExpect(status().isNotFound());

        verify(service).update(eq(TestObjects.id), any(), any());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateFood_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(patch(baseUrl + "/{id}", 99)).andExpect(status().isForbidden());
    }

    @Test
    void testUpdateFood_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(patch(baseUrl + "/{id}", 99)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteFood_AsAdmin_ReturnOk() throws Exception {
        when(service.delete(TestObjects.id)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        MvcResult result = mockMvc.perform(delete(baseUrl + "/{id}", TestObjects.id))
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteFood_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 99)).andExpect(status().isForbidden());
    }

    @Test
    void testDeleteFood_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 99)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "ADMIN"
    )
    void testGetAllReports_ReturnAllReports() throws Exception {
        List<FoodReportResponse> foods = Arrays.asList(TestObjects.foodReport1.toResponse(), TestObjects.foodReport2.toResponse());
        Pageable pageable = PageRequest.of(1, 10);
        Page<FoodReportResponse> mockPage = new PageImpl<>(foods, pageable, foods.size());

        // Mock service layer
        when(service.getAllReports(any(Integer.class), any(Integer.class)))
                .thenReturn(new ResponseEntity<>(mockPage, HttpStatus.OK));

        // Act and Assert
        mockMvc.perform(get(baseUrl + "/report")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", CoreMatchers.is(2)))
                .andExpect(jsonPath("$.content[0].reason", CoreMatchers.is("Incorrect values")))
                .andExpect(jsonPath("$.content[1].status", CoreMatchers.is(ReportStatus.PENDING.toString())));
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
        when(service.reviewReport(any(UUID.class), any(UUID.class), any(FoodReportReviewRequest.class)))
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
