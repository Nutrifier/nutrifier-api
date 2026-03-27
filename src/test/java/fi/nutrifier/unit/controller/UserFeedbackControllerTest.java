package fi.nutrifier.unit.controller;

import fi.nutrifier.controllers.UserFeedbackController;
import fi.nutrifier.dto.UserFeedbackCreateRequest;
import fi.nutrifier.services.UserFeedbackService;
import fi.nutrifier.unit.utils.TestObjects;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserFeedbackController.class)
@ActiveProfiles("test")
public class UserFeedbackControllerTest extends ControllerTestInterface<UserFeedbackService> {

    protected UserFeedbackControllerTest() {
        super("/api/user-feedback");
    }

    @Test
    public void testCreateFeedback_ReturnOk() throws Exception {
        when(service.createFeedback(any(UUID.class), any(UserFeedbackCreateRequest.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.userFeedback))
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.id1.toString()))))
                .andExpect(status().isCreated());

        verify(service).createFeedback(any(UUID.class), any(UserFeedbackCreateRequest.class));
    }

    @Test
    public void testCreateFeedback_WithoutAuth_ReturnOk() throws Exception {
        mockMvc.perform(get(baseUrl)
                    .with(csrf()) // MockMvc expects csrf is in use
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(TestObjects.userFeedback)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(service);
    }
}
