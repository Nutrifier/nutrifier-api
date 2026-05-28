package fi.nutrifier.unit.controller.admin;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.admin.AdminAuditController;
import fi.nutrifier.dto.AuditResponse;
import fi.nutrifier.services.AuditService;
import fi.nutrifier.unit.controller.ControllerTestInterface;
import fi.nutrifier.unit.utils.TestObjects;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminAuditController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
class AdminAuditControllerTest extends ControllerTestInterface<AuditService> {

    protected AdminAuditControllerTest() {
        super("/api/v1/admin/audit");
    }

    private Page<AuditResponse> mockPage() {
        List<AuditResponse> auditLogs = List.of(TestObjects.auditLog1.toResponse());
        return new PageImpl<>(auditLogs, PageRequest.of(0, 10), 1);
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "ADMIN"
    )
    void testGetAll_NoParams_ReturnAllReports() throws Exception {

        when(service.getAll(anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(mockPage()));

        mockMvc.perform(get(baseUrl)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(service).getAll(0, 10);
    }


    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "ADMIN"
    )
    void testGetAll_WithUserId_ReturnAllReports() throws Exception {

        when(service.getAllByUserId(any(UUID.class), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(mockPage()));

        mockMvc.perform(get(baseUrl)
                        .param("page", "0")
                        .param("size", "10")
                        .param("userId", TestObjects.id1.toString()))
                .andExpect(status().isOk());

        verify(service).getAllByUserId(eq(TestObjects.id1), eq(0), eq(10));
    }

    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "ADMIN"
    )
    void testGetAll_WithCategory_ReturnAllReports() throws Exception {

        when(service.getAllByCategory(anyString(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(mockPage()));

        mockMvc.perform(get(baseUrl)
                        .param("page", "0")
                        .param("size", "10")
                        .param("category", "AUTH"))
                .andExpect(status().isOk());

        verify(service).getAllByCategory(anyString(), eq(0), eq(10));
    }


    @Test
    @WithMockUser(
            username = "550e8400-e29b-41d4-a716-446655440000",
            roles = "ADMIN"
    )
    void testGetAll_WithAllParams_ReturnAllReports() throws Exception {

        when(service.getAllByUserIdAndCategory(any(UUID.class), anyString(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(mockPage()));

        mockMvc.perform(get(baseUrl)
                        .param("page", "0")
                        .param("size", "10")
                        .param("userId", TestObjects.id1.toString())
                        .param("category", "AUTH")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", CoreMatchers.is(1)))
                .andExpect(jsonPath("$.content[0].action", CoreMatchers.is("Logged in")))
                .andExpect(jsonPath("$.content[0].category", CoreMatchers.is("AUTH")));

        verify(service).getAllByUserIdAndCategory(eq(TestObjects.id1), anyString(), eq(0), eq(10));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAll_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl)).andExpect(status().isForbidden());
        verifyNoInteractions(service);
    }

    @Test
    void testGetAll_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl)).andExpect(status().isForbidden());
        verifyNoInteractions(service);
    }
}
