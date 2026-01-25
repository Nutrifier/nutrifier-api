package fi.nutrifier.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.nutrifier.controllers.UserController;
import fi.nutrifier.controllers.UserGoalsController;
import fi.nutrifier.entities.UserGoals;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserGoalsController.class)
@ActiveProfiles("test")
public class UserGoalsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserGoalsService userGoalsService;

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
    public void testGetUsersGoals_ReturnOk() throws Exception {
        String id = UUID.randomUUID().toString();
        TestObjects.user1.setId(id); // Mock id generation

        TestObjects.user1.setGoals(TestObjects.goals);

        when(userGoalsService.getUserGoals(anyString()))
                .thenReturn(new ResponseEntity<>(TestObjects.user1.getGoals(), HttpStatus.OK));

        mockMvc.perform(get("/api/users/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.userId1))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.reasoning").value("Just for fun!"))
            .andExpect(jsonPath("$.targetWeight").value(60.0))
            .andExpect(jsonPath("$.reachedDate").isEmpty());
    }

    @Test
    @WithMockUser
    public void testUpdateUsersGoals_ReturnOk() throws Exception {
        TestObjects.goals.setTargetWeight(140.0);
        TestObjects.user1.setGoals(TestObjects.goals);

        when(userGoalsService.update(anyString(), any()))
                .thenReturn(new ResponseEntity<>(TestObjects.user1.getGoals(), HttpStatus.OK));

        mockMvc.perform(patch("/api/users/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.goals))
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.userId1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reasoning").value("Just for fun!"))
                .andExpect(jsonPath("$.targetWeight").value(140.0))
                .andExpect(jsonPath("$.reachedDate").isEmpty());
    }
}
