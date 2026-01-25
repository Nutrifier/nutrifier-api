package fi.nutrifier.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.nutrifier.controllers.UserController;
import fi.nutrifier.controllers.UserMealPlanController;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserMealPlanController.class)
@ActiveProfiles("test")
public class UserMealPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserMealPlanService userMealPlanService;

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
    public void testGetUsersMealPlans_ReturnOk() throws Exception {
        String id = UUID.randomUUID().toString();
        TestObjects.user1.setId(id); // Mock id generation

        TestObjects.user1.setMealPlans(TestObjects.mealPlanList);

        when(userMealPlanService.getAllByUserId(anyString()))
                .thenReturn(new ResponseEntity<>(TestObjects.user1.getMealPlans(), HttpStatus.OK));

        mockMvc.perform(get("/api/users/meal-plan")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.userId1))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value("My Meal Plan"))
            .andExpect(jsonPath("$[0].periods[0].startDate").value(TestObjects.date.toString()));
    }

    @Test
    @WithMockUser
    public void testAddUsersMealPlans_ReturnOk() throws Exception {
        TestObjects.mealPlan.setName("My NEW Meal Plan");
        TestObjects.mealPlanList.add(TestObjects.mealPlan);
        TestObjects.user1.setMealPlans(TestObjects.mealPlanList);

        when(userMealPlanService.create(anyString(), any())).thenReturn(new ResponseEntity<>(TestObjects.user1.getMealPlans().getFirst(), HttpStatus.CREATED));

        mockMvc.perform(post("/api/users/meal-plan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.mealPlan))
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.userId1))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("My NEW Meal Plan"))
            .andExpect(jsonPath("$.periods[0].startDate").value(TestObjects.date.toString()));
    }

    @Test
    @WithMockUser
    public void testDeleteMealPlans_ReturnOk() throws Exception {
        String mealPlanId = TestObjects.mealPlan.getId();

        when(userMealPlanService.delete(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(delete("/api/users/meal-plan/" + mealPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.userId1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
