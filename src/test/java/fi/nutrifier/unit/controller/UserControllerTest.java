package fi.nutrifier.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.nutrifier.controllers.UserController;
import fi.nutrifier.services.UserService;
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
import java.util.UUID;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

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
    public void testGetUsersSettings_ReturnCreated() throws Exception {
        String id = UUID.randomUUID().toString();
        TestObjects.user1.setId(id); // Mock id generation

        TestObjects.user1.setSettings(TestObjects.settings);

        when(service.getById(anyString())).thenReturn(new ResponseEntity<>(TestObjects.user1.toUser(), HttpStatus.FOUND));

        mockMvc.perform(get("/api/users/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id", CoreMatchers.is(id.toString())))
                .andExpect(jsonPath("$.email", CoreMatchers.is("test@gmail.com")))
                .andExpect(jsonPath("$.settings.language").value("en"))
                .andExpect(jsonPath("$.settings.weightUnit").value("kg"))
                .andExpect(jsonPath("$.settings.weighInReminderEnabled").value(true));
    }

    @Test
    @WithMockUser
    public void testGetUsersGoals_ReturnCreated() throws Exception {
        String id = UUID.randomUUID().toString();
        TestObjects.user1.setId(id); // Mock id generation

        TestObjects.user1.setGoals(TestObjects.goals);

        when(service.getById(anyString())).thenReturn(new ResponseEntity<>(TestObjects.user1.toUser(), HttpStatus.FOUND));

        mockMvc.perform(get("/api/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id", CoreMatchers.is(id.toString())))
                .andExpect(jsonPath("$.email", CoreMatchers.is("test@gmail.com")))
                .andExpect(jsonPath("$.goals.reasoning").value("Just for fun!"))
                .andExpect(jsonPath("$.goals.targetWeight").value(60.0))
                .andExpect(jsonPath("$.goals.reachedDate").isEmpty());

    }

    @Test
    @WithMockUser
    public void testGetUsersMealPlans_ReturnCreated() throws Exception {
        String id = UUID.randomUUID().toString();
        TestObjects.user1.setId(id); // Mock id generation

        TestObjects.user1.setMealPlans(TestObjects.mealPlanList);

        when(service.getById(anyString())).thenReturn(new ResponseEntity<>(TestObjects.user1.toUser(), HttpStatus.FOUND));

        mockMvc.perform(get("/api/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id", CoreMatchers.is(id.toString())))
                .andExpect(jsonPath("$.email", CoreMatchers.is("test@gmail.com")))
                .andExpect(jsonPath("$.mealPlans").isArray())
                .andExpect(jsonPath("$.mealPlans.length()").value(1))
                .andExpect(jsonPath("$.mealPlans[0].name").value("My Meal Plan"))
                .andExpect(jsonPath("$.mealPlans[0].periods[0].startDate").value(TestObjects.date.toString()));
    }
}
