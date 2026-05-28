package fi.nutrifier.unit.controller.admin;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.controllers.admin.AdminUserController;
import fi.nutrifier.dto.UserResponse;
import fi.nutrifier.dto.UserUpdateRequest;
import fi.nutrifier.services.UserService;
import fi.nutrifier.unit.controller.ControllerTestInterface;
import fi.nutrifier.unit.utils.TestObjects;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminUserController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class AdminUserControllerTest extends ControllerTestInterface<UserService> {

    protected AdminUserControllerTest() {
        super("/api/v1/admin/users");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAll_AsAdmin_ReturnLogs() throws Exception {
        List<UserResponse> users = List.of(TestObjects.user1, TestObjects.user2);

        Pageable pageable = PageRequest.of(1, 10);
        Page<UserResponse> mockPage = new PageImpl<>(users, pageable, users.size());

        // Mock service layer
        when(service.getAll(any(Integer.class), any(Integer.class)))
                .thenReturn(new ResponseEntity<>(mockPage, HttpStatus.OK));

        mockMvc.perform(get(baseUrl)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].email").value("test@gmail.com"))
                .andExpect(jsonPath("$.content[1].email").value("test2@gmail.com"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAll_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl)).andExpect(status().isForbidden());
    }

    @Test
    public void testGetAll_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetById_AsAdmin_ReturnOk() throws Exception {
        when(service.getById(TestObjects.id))
                .thenReturn(new ResponseEntity<>(TestObjects.user1, HttpStatus.OK));

        mockMvc.perform(get(baseUrl + "/{id}", TestObjects.id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", CoreMatchers.is("test@gmail.com")))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetById_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}", 1))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetById_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}", 1)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateUser_AsAdmin_ReturnUser() throws Exception {
        TestObjects.user1.setEmail("again@gmail.com");

        // Use eq(1L) to match the exact ID and any(User.class) to allow any User instance.
        when(service.update(eq(TestObjects.id), any(UserUpdateRequest.class)))
                .thenReturn(new ResponseEntity<>(TestObjects.user1, HttpStatus.OK));

        mockMvc.perform(patch(baseUrl + "/{id}", TestObjects.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", CoreMatchers.is("again@gmail.com")))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateUser_AsAdmin_InvalidInput_ReturnBadRequest() throws Exception {
        TestObjects.user1.setEmail("mywebsite.fi");

        mockMvc.perform(patch(baseUrl + "/{id}", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestObjects.user1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUpdateUser_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(patch(baseUrl)).andExpect(status().isForbidden());

    }

    @Test
    public void testUpdateUser_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(patch(baseUrl)).andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteUser_AsAdmin_ReturnEmpty() throws Exception {
        when(service.delete(TestObjects.id)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        MvcResult result = mockMvc.perform(delete(baseUrl + "/{id}", TestObjects.id)).andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteUser_AsUser_ReturnForbidden() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 1)).andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteUser_AsNobody_ReturnForbidden() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 1)).andExpect(status().isForbidden());
    }
}
