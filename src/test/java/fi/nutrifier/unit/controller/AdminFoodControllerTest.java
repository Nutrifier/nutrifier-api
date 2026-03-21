package fi.nutrifier.unit.controller;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.admin.AdminFoodController;
import fi.nutrifier.dto.FoodRequest;
import fi.nutrifier.entities.Food;
import fi.nutrifier.services.FoodService;
import fi.nutrifier.unit.utils.TestObjects;
import fi.nutrifier.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
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
class AdminFoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodService service;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private final String baseUrl = "/api/admin/foods";

    @BeforeEach
    public void setup() throws JOSEException {
        TestObjects.reset();
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAll_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl)).andExpect(status().isForbidden());
    }

    @Test
    void testGetAll_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateFood_AllFields_ReturnFood() throws Exception {
        TestObjects.foodResponse1.setName("New name");
        TestObjects.foodResponse1.setCalories(150.0);
        TestObjects.foodResponse1.setBarcode("1536473434");
        TestObjects.foodResponse1.setServingSize(400);
        TestObjects.foodResponse1.setCarbs(128.0);
        TestObjects.foodResponse1.setProtein(24.0);
        TestObjects.foodResponse1.setFat(45.0);

        // Use eq(1L) to match the exact ID and any(Food.class) to allow any User instance.
        when(service.getById(TestObjects.id))
                .thenReturn(new ResponseEntity<>(TestObjects.foodResponse1, HttpStatus.OK));
        when(service.update(eq(TestObjects.id), any(UUID.class), any(FoodRequest.class))).
                thenReturn(new ResponseEntity<>(TestObjects.foodResponse1, HttpStatus.OK));

        mockMvc.perform(patch(baseUrl + "/{id}", TestObjects.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.foodRequest)))
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
        TestObjects.foodRequest.setCalories(-100.0);

        mockMvc.perform(patch(baseUrl + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.foodRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateFood_WhenServiceReturnsNotFound_Returns404() throws Exception {

        when(service.update(eq(TestObjects.id), any(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        mockMvc.perform(patch(baseUrl + "/{id}", TestObjects.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.foodRequest)))
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
}
