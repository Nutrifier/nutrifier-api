package com.example.backend.unit.service;

import com.example.backend.config.RsaKeyConfig;
import com.example.backend.config.SecurityConfig;
import com.example.backend.dto.UserDto;
import com.example.backend.entities.Log;
import com.example.backend.entities.Role;
import com.example.backend.entities.User;
import com.example.backend.exceptions.EncryptionKeyException;
import com.example.backend.exceptions.FailedCryptionException;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.UserService;
import com.example.backend.unit.utils.TestObjects;
import com.example.backend.utils.JwtTokenUtil;
import com.example.backend.utils.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setup() {
        TestObjects.reset();
        repository.deleteAll();
    }

    @Test
    public void testSaveUser_ReturnsUser() {
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<User> response = service.create(TestObjects.user1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("test@gmail.com", response.getBody().getEmail());
        assertNull(response.getBody().getPassword());
    }

    @Test
    public void testFindById_ReturnsUser() throws FailedCryptionException, EncryptionKeyException {
        // The service expects the email to be encrypted
        TestObjects.user1.setEmail(SecurityUtil.encrypt(TestObjects.user1.getEmail()));

        when(repository.findById(TestObjects.id)).thenReturn(Optional.ofNullable(TestObjects.user1.toUser()));

        ResponseEntity<User> response = service.getById(TestObjects.id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test@gmail.com", response.getBody().getEmail());
        assertNull(response.getBody().getPassword());
    }

    @Test
    public void testFindAll_ReturnsMultipleUsers() throws FailedCryptionException, EncryptionKeyException {
        // The service expects the emails to be encrypted
        String email = SecurityUtil.encrypt("maija@gmail.com");

        User user1 = new User(UUID.randomUUID(), email, "password", Role.ROLE_USER);
        User user2 = new User(UUID.randomUUID(), email, "password", Role.ROLE_USER);

        List<User> users = List.of(user1, user2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> mockPage = new PageImpl<>(users, pageable, users.size());

        when(repository.findAll(pageable)).thenReturn(mockPage);

        ResponseEntity<Page<User>> response = service.getAll(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Page<User> page = response.getBody();
        assertNotNull(page);
        assertEquals(2, page.getTotalElements());

        List<User> resUsers = page.getContent();

        assertFalse(resUsers.isEmpty());
        assertNull(resUsers.get(0).getPassword());
        assertNull(resUsers.get(1).getPassword());
    }

    @Test
    public void testUpdateUser_ReturnsUser() {
        when(repository.findById(TestObjects.id)).thenReturn(Optional.of(TestObjects.user1.toUser()));
        when(repository.save(any(User.class))).thenReturn(TestObjects.user1.toUser());

        ResponseEntity<User> response = service.update(TestObjects.id, TestObjects.user1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test@gmail.com", response.getBody().getEmail());
        assertNull(response.getBody().getPassword());
    }

    @Test
    public void testDeleteUser_ReturnsNullBody() {
        doNothing().when(repository).deleteById(TestObjects.id);

        ResponseEntity<User> response = service.delete(TestObjects.id);

        verify(repository, times(1)).deleteById(TestObjects.id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testLoginSuccess() {
        // Service expects a hashed password
        TestObjects.user1.setPassword(SecurityUtil.hashPassword(TestObjects.user1.getPassword()));
        when(repository.findByEmail(any(String.class))).thenReturn(Optional.of(TestObjects.user1.toUser()));

        ResponseEntity<User> response = service.login("test@gmail.com", "password");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        //assertTrue(response.getBody().getId() > 0);
        assertEquals("test@gmail.com", response.getBody().getEmail());
        assertNull(response.getBody().getPassword());
    }

    @Test
    public void testLoginFail() throws Exception {
        // Service expects a hashed password
        TestObjects.user1.setPassword(SecurityUtil.hashPassword(TestObjects.user1.getPassword()));
        when(repository.findById(TestObjects.id)).thenReturn(Optional.of(TestObjects.user1.toUser()));

        ResponseEntity<User> response = service.login("test@gmail.com", "wrong_password");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testIsEmailTaken_ReturnOk() throws Exception {
        TestObjects.user1.setEmail(SecurityUtil.encrypt(TestObjects.user1.getEmail()));
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(TestObjects.user1.toUser()));

        ResponseEntity<Boolean> response = service.isEmailTaken("test@gmail.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testIsEmailTaken_ReturnNotFound() throws Exception {
        TestObjects.user1.setEmail(SecurityUtil.encrypt(TestObjects.user1.getEmail()));
        when(repository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        ResponseEntity<Boolean> response = service.isEmailTaken("test@gmail.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
