package fi.nutrifier.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.nutrifier.controllers.UserController;
import fi.nutrifier.controllers.UserWeightController;
import fi.nutrifier.dto.CreateWeighInRequest;
import fi.nutrifier.entities.WeightEntry;
import fi.nutrifier.services.*;
import fi.nutrifier.unit.utils.TestObjects;
import fi.nutrifier.utils.JwtTokenUtil;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserWeightController.class)
@ActiveProfiles("test")
public class UserWeightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserWeightService userWeightService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setup() {
        TestObjects.reset();
    }

    @Test
    @WithMockUser
    public void testGetUsersWeightEntries_ReturnOk() throws Exception {
        String id = UUID.randomUUID().toString();
        TestObjects.user1.setId(id); // Mock id generation

        TestObjects.user1.setMealPlans(TestObjects.mealPlanList);

        when(userWeightService.getAllByUserId(anyString()))
                .thenReturn(new ResponseEntity<>(TestObjects.user1.getWeightEntries(), HttpStatus.FOUND));

        mockMvc.perform(get("/api/users/weight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.userId1))))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$[0].weight", CoreMatchers.is(84.0)));
    }

    @Test
    @WithMockUser
    public void testAddUsersWeightEntries_ReturnOk() throws Exception {
        WeightEntry newWeightEntry = new WeightEntry();
        newWeightEntry.setWeight(134.5);
        newWeightEntry.setDate(LocalDateTime.now());
        TestObjects.user1.setWeightEntries(List.of(newWeightEntry));
        CreateWeighInRequest newRequest = new CreateWeighInRequest(134.5);

        when(userWeightService.create(anyString(), any()))
                .thenReturn(new ResponseEntity<>(TestObjects.user1.getWeightEntries(), HttpStatus.CREATED));

        mockMvc.perform(post("/api/users/weight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRequest))
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.userId1))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].weight", CoreMatchers.is(134.5)));
    }
}
