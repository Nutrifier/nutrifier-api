package fi.nutrifier.unit.controller;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.admin.AdminFoodEntryController;
import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.services.FoodEntryService;
import fi.nutrifier.unit.utils.TestObjects;
import fi.nutrifier.utils.JwtTokenUtil;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminFoodEntryController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class AdminFoodEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodEntryService service;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private final String baseUrl = "/api/admin/food-entries";

    @BeforeEach
    public void setup() {
        TestObjects.reset();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllByUserId_AsAdmin_ReturnLogs() throws Exception {
        List<FoodEntry> foodEntries = List.of(TestObjects.foodEntry1, TestObjects.foodEntry2);

        Pageable pageable = PageRequest.of(1, 10);
        Page<FoodEntry> mockPage = new PageImpl<>(foodEntries, pageable, foodEntries.size());

        // Mock service layer
        when(service.getAllByUserId(any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(new ResponseEntity<>(mockPage, HttpStatus.OK));

        mockMvc.perform(get(baseUrl)
                .param("userId", TestObjects.userId1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()", CoreMatchers.is(2)))
            .andExpect(jsonPath("$.content[0].meal", CoreMatchers.is("BREAKFAST")))
            .andExpect(jsonPath("$.content[1].meal", CoreMatchers.is("LUNCH")));
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
        when(service.getById(TestObjects.id)).thenReturn(new ResponseEntity<>(TestObjects.foodEntry1, HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/{id}", TestObjects.id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", CoreMatchers.is(TestObjects.foodEntry1.getAmount())))
                .andExpect(jsonPath("$.meal", CoreMatchers.is(TestObjects.foodEntry1.getMeal())));

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

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetByUserId_AsAdmin_ReturnOk() throws Exception {
        List<FoodEntry> foodEntries = List.of(TestObjects.foodEntry1, TestObjects.foodEntry2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<FoodEntry> mockPage = new PageImpl<>(foodEntries, pageable, foodEntries.size());

        when(service.getAllByUserId(TestObjects.userId1, 0, 10))
                .thenReturn(new ResponseEntity<>(mockPage, HttpStatus.OK));

        mockMvc.perform(get(baseUrl)
                .param("userId", TestObjects.userId1)
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].amount").value(22.0))
            .andExpect(jsonPath("$.content[1].meal").value("LUNCH"));

        verify(service, times(1)).getAllByUserId(TestObjects.userId1, 0, 10);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetByUserId_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl + "/by-user/{id}", 1)).andExpect(status().isForbidden());
    }

    @Test
    public void testGetByUserId_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl + "/by-user/{id}", 1)).andExpect(status().isForbidden());
    }
}
