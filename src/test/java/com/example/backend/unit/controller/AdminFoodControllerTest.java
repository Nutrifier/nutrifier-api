package com.example.backend.unit.controller;

import com.example.backend.config.SecurityConfig;
import com.example.backend.controllers.FoodController;
import com.example.backend.controllers.admin.AdminFoodController;
import com.example.backend.entities.Food;
import com.example.backend.services.FoodService;
import com.example.backend.unit.utils.TestObjects;
import com.example.backend.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        TestObjects.food1.setName("New name");
        TestObjects.food1.setCalories(150.0);
        TestObjects.food1.setBarcode("1536473434");
        TestObjects.food1.setServingSize(400);
        TestObjects.food1.setCarbs(128.0);
        TestObjects.food1.setProtein(24.0);
        TestObjects.food1.setFat(45.0);

        // Use eq(1L) to match the exact ID and any(Food.class) to allow any User instance.
        when(service.getById(TestObjects.id)).thenReturn(new ResponseEntity<>(TestObjects.food1, HttpStatus.OK));
        when(service.update(eq(TestObjects.id), any(Food.class))).thenReturn(new ResponseEntity<>(TestObjects.food1, HttpStatus.OK));

        mockMvc.perform(patch(baseUrl + "/{id}", TestObjects.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.food1)))
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
    void testUpdateFood_InvalidObject_ReturnsNotFound() throws Exception {
        TestObjects.food1.setCalories(-100.0);

        mockMvc.perform(patch(baseUrl + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.food1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateFood_InvalidId_ReturnsNotFound() throws Exception {
        when(service.getById(TestObjects.id)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        mockMvc.perform(patch(baseUrl + "/{id}", TestObjects.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.food1)))
                .andExpect(status().isNotFound());
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
