package fi.nutrifier.unit.service;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.dto.RegisterRequest;
import fi.nutrifier.dto.UserResponse;
import fi.nutrifier.dto.UserUpdateRequest;
import fi.nutrifier.entities.*;
import fi.nutrifier.enums.GoalType;
import fi.nutrifier.enums.Sex;
import fi.nutrifier.exceptions.EncryptionKeyException;
import fi.nutrifier.exceptions.FailedCryptionException;
import fi.nutrifier.repositories.*;
import fi.nutrifier.services.UserService;
import fi.nutrifier.services.reference.DietService;
import fi.nutrifier.services.reference.RoleService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static fi.nutrifier.unit.utils.TestObjects.ROLE_REGULAR;
import static fi.nutrifier.unit.utils.TestObjects.toUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private DietService dietService;

    @Mock
    private RoleService roleService;

    @Mock
    private UserRepository repository;

    @Mock
    private UserSettingsRepository userSettingsRepository;

    @Mock
    private WeightRepository weightRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private GoalsRepository goalsRepository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setup() {
        TestObjects.reset();
        repository.deleteAll();
    }

    @Test
    public void testSaveUser_ReturnsUser() throws FailedCryptionException, EncryptionKeyException {
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userSettingsRepository.save(any(Settings.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(weightRepository.save(any(WeightEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(goalsRepository.save(any(Goals.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(dietService.of(anyString())).thenReturn(TestObjects.DIET_STANDARD);
        when(roleService.of(anyString())).thenReturn(TestObjects.ROLE_REGULAR);

        RegisterRequest registerRequest = new RegisterRequest(
                TestObjects.userResponse1.getEmail(),
                "Qwerty123!",
                Sex.FEMALE,
                20,
                170,
                TestObjects.ACTIVITY_LEVEL_SEDENTARY,
                GoalType.MAINTAIN,
                50.0,
                50.0,
                LocalDate.now().plusMonths(3)
        );

        ResponseEntity<UserResponse> response = service.create(registerRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("test@gmail.com", response.getBody().getEmail());
    }

    @Test
    public void testFindById_ReturnsUser() throws FailedCryptionException, EncryptionKeyException {
        // The service expects the email to be encrypted
        TestObjects.userResponse1.setEmail(SecurityUtil.encrypt(TestObjects.userResponse1.getEmail()));

        when(repository.findById(TestObjects.id)).thenReturn(Optional.of(toUser(TestObjects.userResponse1, ROLE_REGULAR)));

        ResponseEntity<UserResponse> response = service.getById(TestObjects.id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test@gmail.com", response.getBody().getEmail());
    }

    @Test
    public void testFindAll_ReturnsMultipleUsers() throws FailedCryptionException, EncryptionKeyException {
        // The service expects the emails to be encrypted
        String email = SecurityUtil.encrypt("maija@gmail.com");

        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setEmail(email);
        user1.setPassword("password");
        user1.setRole(TestObjects.ROLE_REGULAR);

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setEmail(email);
        user2.setPassword("password");
        user2.setRole(TestObjects.ROLE_REGULAR);

        List<User> users = List.of(user1, user2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> mockPage = new PageImpl<>(users, pageable, users.size());

        when(repository.findAll(pageable)).thenReturn(mockPage);

        ResponseEntity<Page<UserResponse>> response = service.getAll(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Page<UserResponse> page = response.getBody();
        assertNotNull(page);
        assertEquals(2, page.getTotalElements());

        List<UserResponse> resUsers = page.getContent();

        assertFalse(resUsers.isEmpty());
    }

    @Test
    public void testUpdateUser_ReturnsUser() throws FailedCryptionException, EncryptionKeyException {
        when(repository.findById(TestObjects.id)).thenReturn(Optional.of(toUser(TestObjects.userResponse1, ROLE_REGULAR)));
        when(repository.save(any(User.class))).thenReturn(toUser(TestObjects.userResponse1, ROLE_REGULAR));

        ResponseEntity<UserResponse> response = service.update(TestObjects.id, new UserUpdateRequest(TestObjects.userResponse1.getEmail()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test@gmail.com", response.getBody().getEmail());
    }

    @Test
    public void testDeleteUser_ReturnsNullBody() {
        when(repository.existsById(any(UUID.class))).thenReturn(true);
        doNothing().when(repository).deleteById(TestObjects.id);

        ResponseEntity<String> response = service.delete(TestObjects.id);

        verify(repository, times(1)).deleteById(TestObjects.id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testLoginSuccess() throws FailedCryptionException, EncryptionKeyException {
        // Service expects a hashed password
        User user = toUser(TestObjects.userResponse1, ROLE_REGULAR);
        String hashedPassword = SecurityUtil.hashPassword("password");
        user.setPassword(hashedPassword);
        when(repository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        ResponseEntity<UserResponse> response = service.login("test@gmail.com", "password");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        //assertTrue(response.getBody().getId() > 0);
        assertEquals("test@gmail.com", response.getBody().getEmail());
    }

    @Test
    public void testLoginFail() throws Exception {
        // Service expects a hashed password
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(toUser(TestObjects.userResponse1, ROLE_REGULAR)));
        when(repository.findById(TestObjects.id)).thenReturn(Optional.of(toUser(TestObjects.userResponse1, ROLE_REGULAR)));

        ResponseEntity<UserResponse> response = service.login("test@gmail.com", "wrong_password");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testIsEmailTaken_ReturnOk() throws Exception {
        TestObjects.userResponse1.setEmail(SecurityUtil.encrypt(TestObjects.userResponse1.getEmail()));
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(toUser(TestObjects.userResponse1, ROLE_REGULAR)));

        ResponseEntity<Boolean> response = service.isEmailTaken("test@gmail.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testIsEmailTaken_ReturnNotFound() throws Exception {
        TestObjects.userResponse1.setEmail(SecurityUtil.encrypt(TestObjects.userResponse1.getEmail()));
        when(repository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        ResponseEntity<Boolean> response = service.isEmailTaken("test@gmail.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
