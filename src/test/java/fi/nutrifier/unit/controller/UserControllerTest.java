package fi.nutrifier.unit.controller;

import fi.nutrifier.controllers.UserController;
import fi.nutrifier.services.*;
import fi.nutrifier.unit.utils.TestObjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTest extends ControllerTestInterface<UserService> {

    protected UserControllerTest() {
        super("/api/users");
    }

    @Test
    public void testGetUser_ReturnOk() throws Exception {
        when(service.getById(eq(TestObjects.id1)))
                .thenReturn(ResponseEntity.ok(TestObjects.user1));

        mockMvc.perform(get(baseUrl)
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("test@gmail.com"));

        verify(service).getById(any(UUID.class));
    }

    @Test
    public void testGetUser_WithoutAuth_ReturnOk() throws Exception {
        mockMvc.perform(get(baseUrl)
                    .with(csrf())) // MockMvc expects csrf is in use
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(service);
    }
}
