package fi.nutrifier.unit.controller.admin;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.admin.AdminUserFeedbackController;
import fi.nutrifier.dto.*;
import fi.nutrifier.services.UserFeedbackService;
import fi.nutrifier.unit.controller.ControllerTestInterface;
import fi.nutrifier.unit.utils.TestObjects;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminUserFeedbackController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class AdminUserFeedbackControllerTest extends ControllerTestInterface<UserFeedbackService> {

    protected AdminUserFeedbackControllerTest() {
        super("/api/admin/user-feedback");
    }

    private Page<UserFeedbackResponse> mockPage() {
        List<UserFeedbackResponse> list = List.of(TestObjects.userFeedback.toResponse());
        return new PageImpl<>(list, PageRequest.of(0, 10), 1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAll_AsAdmin_ReturnFeedbacks() throws Exception {
        when(service.getAllFeedbacks(any(Integer.class), any(Integer.class)))
                .thenReturn(new ResponseEntity<>(mockPage(), HttpStatus.OK));

        mockMvc.perform(get(baseUrl)
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()", CoreMatchers.is(1)))
            .andExpect(jsonPath("$.content[0].title", CoreMatchers.is("My feedback")));

        verify(service).getAllFeedbacks(anyInt(), anyInt());
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
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "ADMIN"
    )
    void testReviewFeedback_isSuccessful() throws Exception {
        when(service.reviewFeedback(any(UUID.class), any(UUID.class), any(UserFeedbackReviewRequest.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(patch(baseUrl + "/{id}", TestObjects.userFeedback.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestObjects.userFeedback)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testReviewReport_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(patch(baseUrl + "/{id}", 99)).andExpect(status().isForbidden());
    }

    @Test
    void testReviewReport_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(patch(baseUrl + "/{id}", 99)).andExpect(status().isForbidden());
    }
}
