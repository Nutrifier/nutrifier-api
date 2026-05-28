package fi.nutrifier.unit.controller;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.FoodController;
import fi.nutrifier.dto.FoodReportCreateRequest;
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
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
class FoodControllerTest extends ControllerTestInterface<FoodService> {

    protected FoodControllerTest() {
        super("/api/v1/foods");
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "USER"
    )
    void testCreateFood_ReturnCreated() throws Exception {
        when(service.create(any(FoodRequest.class), any(UUID.class)))
                .thenReturn(new ResponseEntity<>(TestObjects.food1.toResponse(), HttpStatus.CREATED));

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.food1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(TestObjects.food1.getName())))
                .andExpect(jsonPath("$.calories", CoreMatchers.is(TestObjects.food1.getCalories())));

        verify(service).create(any(FoodRequest.class), any(UUID.class));
    }

    @ParameterizedTest
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "USER"
    )
    @ValueSource(strings = { "name", "calories", "servingSize" })
    void testCreateFood_InvalidFields_ReturnBadRequest(String missingField) throws Exception {
        when(service.create(any(FoodRequest.class), any(UUID.class)))
                .thenReturn(new ResponseEntity<>(TestObjects.food1.toResponse(), HttpStatus.CREATED));

        switch (missingField) {
            case "name":
                TestObjects.food1.setName(null);
                break;
            case "calories":
                TestObjects.food1.setCalories(null);
                break;
            case "servingSize":
                TestObjects.food1.setServingSize(0);
                break;
        }

        mockMvc.perform(post(baseUrl)
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.food1.toRequest())))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(service);
    }

    @Test
    void testCreateFood_WithoutAuth_ReturnForbidden() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .with(csrf()) // MockMvc expects csrf is in use
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.food1.toRequest())))
                .andExpect(status().isForbidden());

        verifyNoMoreInteractions(service);
    }

    @Test
    @WithMockUser
    void testGetById_ReturnOk() throws Exception {
        when(service.getById(TestObjects.id))
                .thenReturn(new ResponseEntity<>(TestObjects.food1.toResponse(), HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/{id}", TestObjects.id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(TestObjects.food1.getName())))
                .andExpect(jsonPath("$.calories", CoreMatchers.is(TestObjects.food1.getCalories())));

        verify(service).getById(any(UUID.class));
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
        List<FoodResponse> foods = List.of(TestObjects.food1.toResponse());
        Page<FoodResponse> foodPage = new PageImpl<>(foods);

        when(service.getFoodsByQuery(anyInt(), anyInt(), anyString()))
                .thenReturn(new ResponseEntity<>(foodPage, HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/query")
                .param("query", "ka"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Kanan rintafilee"))
                .andExpect(jsonPath("$.content[0].calories").value(250.0));

        verify(service).getFoodsByQuery(anyInt(), anyInt(), anyString());
    }

    @Test
    @WithMockUser
    void testGetByBarcode_ReturnOk() throws Exception {
        when(service.getFoodsByBarcode(anyString()))
                .thenReturn(new ResponseEntity<>(List.of(TestObjects.food1.toResponse()), HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/barcode")
                        .param("query", "1234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Kanan rintafilee"))
                .andExpect(jsonPath("$[0].calories").value(250.0));

        verify(service).getFoodsByBarcode(anyString());
    }

    @Test
    @WithMockUser
    void testGetAll_ReturnsFoods() throws Exception {
        List<FoodResponse> foods = Arrays.asList(TestObjects.food1.toResponse(), TestObjects.food2.toResponse());
        Pageable pageable = PageRequest.of(1, 10);
        Page<FoodResponse> mockPage = new PageImpl<>(foods, pageable, foods.size());

        // Mock service layer
        when(service.getAll(anyInt(), anyInt()))
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

        verify(service).getAll(anyInt(), anyInt());
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "USER"
    )
    void testGetRecentFoods_ReturnOk() throws Exception {
        when(service.getRecentFoods(any(UUID.class)))
                .thenReturn(new ResponseEntity<>(List.of(TestObjects.food1.toResponse()), HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Kanan rintafilee"))
                .andExpect(jsonPath("$[0].calories").value(250.0));

        verify(service).getRecentFoods(any(UUID.class));
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "USER"
    )
    void testMarkFoodAsFavourite_ReturnOk() throws Exception {
        when(service.markAsFavourite(any(UUID.class), any(UUID.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(post(baseUrl + "/{id}/favourite", TestObjects.food1.getId().toString()))
                .andExpect(status().isOk());

        verify(service).markAsFavourite(any(UUID.class), any(UUID.class));
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "USER"
    )
    void testRemoveFavourite_ReturnOk() throws Exception {
        when(service.removeFavourite(any(UUID.class), any(UUID.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(delete(baseUrl + "/{id}/favourite", TestObjects.food1.getId().toString()))
                .andExpect(status().isOk());

        verify(service).removeFavourite(any(UUID.class), any(UUID.class));
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "USER"
    )
    void testGetAllFavourites_ReturnOk() throws Exception {
        when(service.getAllFavourites(any(UUID.class)))
                .thenReturn(new ResponseEntity<>(List.of(TestObjects.food1.toResponse()), HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/favourites"))
                .andExpect(status().isOk());

        verify(service).getAllFavourites(any(UUID.class));
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "USER"
    )
    void testReportFood_ReturnCreated() throws Exception {
        when(service.report(any(UUID.class), any(UUID.class), any(FoodReportCreateRequest.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        mockMvc.perform(post(baseUrl + "/{id}/report", TestObjects.foodReport1.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.foodReport1.toCreateRequest())))
                .andExpect(status().isCreated());

        verify(service).report(any(UUID.class), any(UUID.class), any(FoodReportCreateRequest.class));
    }
}
