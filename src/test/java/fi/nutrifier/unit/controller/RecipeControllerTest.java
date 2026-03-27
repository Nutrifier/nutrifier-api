package fi.nutrifier.unit.controller;

import fi.nutrifier.controllers.RecipeController;
import fi.nutrifier.dto.RecipeReportCreateRequest;
import fi.nutrifier.dto.RecipeRequest;
import fi.nutrifier.dto.RecipeResponse;
import fi.nutrifier.services.RecipeService;
import fi.nutrifier.unit.utils.TestObjects;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
@ActiveProfiles("test")
public class RecipeControllerTest extends ControllerTestInterface<RecipeService> {

    protected RecipeControllerTest() {
        super("/api/recipes");
    }

    @Test
    @WithMockUser
    public void testCreate_ReturnCreated() throws Exception {
        when(service.create(any(RecipeRequest.class), any(UUID.class)))
                .thenReturn(new ResponseEntity<>(TestObjects.recipe.toResponse(), HttpStatus.CREATED));

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.recipe))
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("My recipe"));

        verify(service).create(any(RecipeRequest.class), any(UUID.class));
    }

    @Test
    public void testCreate_WithoutAuth_ReturnUnauthorized() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .with(csrf()) // MockMvc expects csrf is in use
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.recipe)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(service);
    }

    @Test
    public void testGetById_ReturnOk() throws Exception {
        when(service.getById(any(UUID.class)))
                .thenReturn(new ResponseEntity<>(TestObjects.recipe.toResponse(), HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/{id}", TestObjects.id1.toString())
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("My recipe"));

        verify(service).getById(any(UUID.class));
    }

    @Test
    public void testGetById_WithoutAuth_ReturnUnauthorized() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}", TestObjects.id1.toString())
                        .with(csrf()) // MockMvc expects csrf is in use
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.recipe)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(service);
    }

    @Test
    public void testMarkFavourite_ReturnOk() throws Exception {
        when(service.markAsFavourite(any(UUID.class), any(UUID.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(post(baseUrl + "/{id}/favourite", TestObjects.id1.toString())
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
                .andExpect(status().isOk());

        verify(service).markAsFavourite(any(UUID.class), any(UUID.class));
    }

    @Test
    public void testMarkFavourite_WithoutAuth_ReturnUnauthorized() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}/favourite", TestObjects.id1.toString())
                        .with(csrf())) // MockMvc expects csrf is in use
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(service);
    }

    @Test
    public void testRemoveFavourite_ReturnOk() throws Exception {
        when(service.removeFavourite(any(UUID.class), any(UUID.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(delete(baseUrl + "/{id}/favourite", TestObjects.id1.toString())
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
                .andExpect(status().isOk());

        verify(service).removeFavourite(any(UUID.class), any(UUID.class));
    }

    @Test
    public void testRemoveFavourite_WithoutAuth_ReturnUnauthorized() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}/favourite", TestObjects.id1.toString())
                        .with(csrf())) // MockMvc expects csrf is in use
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(service);
    }

    @Test
    public void testGetAllFavourites_ReturnOk() throws Exception {
        List<RecipeResponse> favourites = new ArrayList<>();
        favourites.add(TestObjects.recipe.toResponse());

        when(service.getAllFavourites(any(UUID.class)))
                .thenReturn(new ResponseEntity<>(favourites, HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/favourites")
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
                .andExpect(status().isOk());

        verify(service).getAllFavourites(any(UUID.class));
    }

    @Test
    public void testGetAllFavourites_WithoutAuth_ReturnUnauthorized() throws Exception {
        mockMvc.perform(get(baseUrl + "/favourites")
                        .with(csrf())) // MockMvc expects csrf is in use
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(service);
    }

    @Test
    public void testReport_ReturnOk() throws Exception {
        when(service.report(any(UUID.class), any(UUID.class), any(RecipeReportCreateRequest.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(post(baseUrl + "/{id}/report", TestObjects.id1.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.recipeReport))
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
                .andExpect(status().isOk());

        verify(service).report(any(UUID.class), any(UUID.class), any(RecipeReportCreateRequest.class));
    }

    @Test
    public void testReport_WithoutAuth_ReturnUnauthorized() throws Exception {
        mockMvc.perform(post(baseUrl + "/{id}/report", TestObjects.id1.toString())
                        .with(csrf()) // MockMvc expects csrf is in use
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.recipeReport)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(service);
    }
}
