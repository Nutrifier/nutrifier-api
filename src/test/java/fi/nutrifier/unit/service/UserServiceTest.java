package fi.nutrifier.unit.service;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.dto.AuthRequest;
import fi.nutrifier.dto.UserDto;
import fi.nutrifier.entities.Role;
import fi.nutrifier.entities.User;
import fi.nutrifier.entities.UserSettings;
import fi.nutrifier.exceptions.EncryptionKeyException;
import fi.nutrifier.exceptions.FailedCryptionException;
import fi.nutrifier.repositories.UserRepository;
import fi.nutrifier.repositories.UserSettingsRepository;
import fi.nutrifier.services.UserService;
import fi.nutrifier.unit.utils.TestObjects;
import fi.nutrifier.utils.JwtTokenUtil;
import fi.nutrifier.utils.SecurityUtil;
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

    @Mock
    private UserSettingsRepository userSettingsRepository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setup() {
        TestObjects.reset();
        repository.deleteAll();
    }

    @Test
    public void testSaveUser_ReturnsUser() {
        when(repository.save(any(User.class))) .thenAnswer(invocation -> invocation.getArgument(0));
        when(userSettingsRepository.save(any(UserSettings.class))) .thenAnswer(invocation -> invocation.getArgument(0));

        AuthRequest authRequest = new AuthRequest(TestObjects.user1.getEmail(), "qwerty");

        ResponseEntity<UserDto> response = service.create(authRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("test@gmail.com", response.getBody().getEmail());
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

        User user1 = new User();
        user1.setId(UUID.randomUUID().toString());
        user1.setEmail(email);
        user1.setPassword("password");
        user1.setRole(Role.REGULAR);

        User user2 = new User();
        user2.setId(UUID.randomUUID().toString());
        user2.setEmail(email);
        user2.setPassword("password");
        user2.setRole(Role.REGULAR);

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
        User user = TestObjects.user1.toUser();
        String hashedPassword = SecurityUtil.hashPassword("password");
        user.setPassword(hashedPassword);
        when(repository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

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
