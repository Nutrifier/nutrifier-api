package fi.nutrifier.unit.controller;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.AuthenticationController;
import fi.nutrifier.dto.AuthRequest;
import fi.nutrifier.dto.UserDto;
import fi.nutrifier.entities.Role;
import fi.nutrifier.services.UserService;
import fi.nutrifier.unit.utils.TestObjects;
import fi.nutrifier.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class AuthenticationControllerTest {

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
    public void testRegister_ReturnCreated() throws Exception {
        String id = UUID.randomUUID().toString();
        TestObjects.user1.setId(id); // Mock id generation

        when(service.isEmailTaken(anyString())).thenReturn(new ResponseEntity<>(false, HttpStatus.NOT_FOUND));
        when(service.create(any(UserDto.class))).thenReturn(new ResponseEntity<>(TestObjects.user1.toUser(), HttpStatus.CREATED));
        when(jwtTokenUtil.generateToken(anyString(), any(Role.class))).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.user1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", CoreMatchers.is("mock-jwt-token")))
                .andExpect(jsonPath("$.userId", CoreMatchers.is(id.toString())))
                .andExpect(jsonPath("$.userEmail", CoreMatchers.is("test@gmail.com")));
    }

    @Test
    public void testLogin_ReturnOk() throws Exception {
        String id = UUID.randomUUID().toString();
        TestObjects.user1.setId(id); // Mock id generation

        when(service.login(anyString(), anyString())).thenReturn(new ResponseEntity<>(TestObjects.user1.toUser(), HttpStatus.OK));
        when(jwtTokenUtil.generateToken(anyString(), any(Role.class))).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AuthRequest("test@gmail.com", "password"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", CoreMatchers.is("mock-jwt-token")))
                .andExpect(jsonPath("$.userId", CoreMatchers.is(id.toString())))
                .andExpect(jsonPath("$.userEmail", CoreMatchers.is("test@gmail.com")));
    }
}
