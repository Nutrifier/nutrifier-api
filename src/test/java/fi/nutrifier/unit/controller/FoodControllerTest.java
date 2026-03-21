package fi.nutrifier.unit.controller;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.FoodController;
import fi.nutrifier.dto.FoodRequest;
import fi.nutrifier.dto.FoodResponse;
import fi.nutrifier.services.FoodService;
import fi.nutrifier.unit.utils.TestObjects;
import fi.nutrifier.utils.JwtTokenUtil;
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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodService service;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private final String baseUrl = "/api/foods";

    @BeforeEach
    public void setup() throws JOSEException {
        TestObjects.reset();
    }

    @Test
    @WithMockUser
    void testCreateFood_ReturnCreated() throws Exception {
        // Use any(Food.class) because the User instance created during JSON deserialization
        // won't match the exact instance in the test setup.
        when(service.create(any(FoodRequest.class), any(UUID.class)))
                .thenReturn(new ResponseEntity<>(TestObjects.foodResponse1, HttpStatus.CREATED));

        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.food1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(TestObjects.food1.getName())))
                .andExpect(jsonPath("$.calories", CoreMatchers.is(TestObjects.food1.getCalories())));
    }

    @Test
    @WithMockUser
    void testGetAll_ReturnsFoods() throws Exception {
        List<FoodResponse> foods = Arrays.asList(TestObjects.foodResponse1, TestObjects.foodResponse2);
        Pageable pageable = PageRequest.of(1, 10);
        Page<FoodResponse> mockPage = new PageImpl<>(foods, pageable, foods.size());

        // Mock service layer
        when(service.getAll(any(Integer.class), any(Integer.class)))
                .thenReturn(new ResponseEntity<>(mockPage, HttpStatus.OK));

        // Act and Assert
        mockMvc.perform(get(baseUrl)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", CoreMatchers.is(2)))
                .andExpect(jsonPath("$.content[0].name", CoreMatchers.is("Kanan rintafilee")))
                .andExpect(jsonPath("$.content[1].name", CoreMatchers.is("Riisi (keitetty)")));
    }

    @ParameterizedTest
    @WithMockUser
    @ValueSource(strings = { "name", "calories", "servingSize" })
    void testCreateFood_InvalidFields_ReturnBadRequest(String missingField) throws Exception {
        when(service.create(any(FoodRequest.class), any(UUID.class)))
                .thenReturn(new ResponseEntity<>(TestObjects.foodResponse1, HttpStatus.CREATED));

        switch (missingField) {
            case "name":
                TestObjects.foodRequest.setName(null);
                break;
            case "calories":
                TestObjects.foodRequest.setCalories(null);
                break;
            case "servingSize":
                TestObjects.foodRequest.setServingSize(0);
                break;
        }

        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.foodRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateFood_WithoutAuth_ReturnForbidden() throws Exception {
        mockMvc.perform(post(baseUrl)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void testGetById_ReturnOk() throws Exception {
        when(service.getById(TestObjects.id))
                .thenReturn(new ResponseEntity<>(TestObjects.foodResponse1, HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/{id}", TestObjects.id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(TestObjects.food1.getName())))
                .andExpect(jsonPath("$.calories", CoreMatchers.is(TestObjects.food1.getCalories())));
    }

    @Test
    @WithMockUser
    void testGetById_ReturnNotFound() throws Exception {
        when(service.getById(TestObjects.id)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        mockMvc.perform(get(baseUrl + "/{id}", TestObjects.id)).andExpect(status().isNotFound());
    }

    @Test
    void testGetById_WithoutAuth_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}", 13)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void testQuery_ReturnOk() throws Exception {
        List<FoodResponse> foods = List.of(TestObjects.foodResponse1);
        Page<FoodResponse> foodPage = new PageImpl<>(foods);

        when(service.getFoodsByQuery(anyInt(), anyInt(), anyString()))
                .thenReturn(new ResponseEntity<>(foodPage, HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/query")
                .param("query", "ka"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Kanan rintafilee"))
                .andExpect(jsonPath("$.content[0].calories").value(250.0));
    }
}
