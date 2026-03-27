package fi.nutrifier.unit.controller;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.AuthenticationController;
import fi.nutrifier.dto.LoginRequest;
import fi.nutrifier.dto.RegisterRequest;
import fi.nutrifier.enums.Role;
import fi.nutrifier.services.UserService;
import fi.nutrifier.unit.utils.TestObjects;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class AuthenticationControllerTest extends ControllerTestInterface<UserService> {

    protected AuthenticationControllerTest() {
        super("/api");
    }

    @Test
    public void testRegister_ReturnCreated() throws Exception {
        UUID id = UUID.randomUUID();
        TestObjects.user1.setId(id); // Mock id generation

        when(service.isEmailTaken(anyString())).thenReturn(new ResponseEntity<>(false, HttpStatus.NOT_FOUND));
        when(service.create(any(RegisterRequest.class))).thenReturn(new ResponseEntity<>(TestObjects.user1, HttpStatus.CREATED));
        when(jwtTokenUtil.generateToken(any(UUID.class), any(Role.class))).thenReturn("mock-jwt-token");

        mockMvc.perform(post(baseUrl + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", CoreMatchers.is("mock-jwt-token")))
                .andExpect(jsonPath("$.userId", CoreMatchers.is(id.toString())));
    }

    @Test
    public void testLogin_ReturnOk() throws Exception {
        UUID id = UUID.randomUUID();
        TestObjects.user1.setId(id); // Mock id generation

        when(service.login(anyString(), anyString())).thenReturn(new ResponseEntity<>(TestObjects.user1, HttpStatus.OK));
        when(jwtTokenUtil.generateToken(any(UUID.class), any(Role.class))).thenReturn("mock-jwt-token");

        mockMvc.perform(post(baseUrl + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest("test@gmail.com", "password"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", CoreMatchers.is("mock-jwt-token")))
                .andExpect(jsonPath("$.userId", CoreMatchers.is(id.toString())));
    }
}
