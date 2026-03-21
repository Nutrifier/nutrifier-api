package fi.nutrifier.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.nutrifier.controllers.UserController;
import fi.nutrifier.services.*;
import fi.nutrifier.unit.utils.TestObjects;
import fi.nutrifier.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setup() {
        TestObjects.reset();
    }

    @Test
    public void testGetUser_ReturnOk() throws Exception {
        when(userService.getById(eq(TestObjects.userId1)))
                .thenReturn(ResponseEntity.ok(TestObjects.user1.toUser()));

        mockMvc.perform(get("/api/users")
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.userId1.toString()))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(TestObjects.userId1))
            .andExpect(jsonPath("$.email").value("test@gmail.com"))
            .andExpect(jsonPath("$.settings").exists());
    }
}
