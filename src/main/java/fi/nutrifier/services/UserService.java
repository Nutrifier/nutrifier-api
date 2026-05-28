package fi.nutrifier.services;

import fi.nutrifier.dto.RegisterRequest;
import fi.nutrifier.dto.UserResponse;
import fi.nutrifier.dto.UserUpdateRequest;
import fi.nutrifier.entities.*;
import fi.nutrifier.enums.Role;
import fi.nutrifier.exceptions.*;
import fi.nutrifier.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import fi.nutrifier.utils.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserSettingsRepository userSettingsRepository;
    private final ProfileRepository profileRepository;
    private final GoalsRepository goalsRepository;
    private final WeightRepository weightRepository;
    private final GoalsService goalsService;

    @Autowired
    public UserService(
            UserRepository repository,
            UserSettingsRepository userSettingsRepository,
            ProfileRepository profileRepository,
            GoalsRepository goalsRepository,
            WeightRepository weightRepository,
            GoalsService goalsService
    ) {
        this.repository = repository;
        this.userSettingsRepository = userSettingsRepository;
        this.profileRepository = profileRepository;
        this.goalsRepository = goalsRepository;
        this.weightRepository = weightRepository;
        this.goalsService = goalsService;
    }

    @Transactional
    public ResponseEntity<UserResponse> create(RegisterRequest registerRequest) throws FailedCryptionException, EncryptionKeyException {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        String encryptedEmail = SecurityUtil.encrypt(registerRequest.getEmail());
        String hashedPassword = SecurityUtil.hashPassword(registerRequest.getPassword());

        User user = new User();
        user.setEmail(encryptedEmail);
        user.setPassword(hashedPassword);
        user.setRole(Role.REGULAR); // Default to regular user
        User savedUser = repository.save(user);

        System.out.println("User service 1");

        // Initialize user settings
        Settings settings = new Settings(
                savedUser.getId(),
                "G",
                "KCAL",
                "FULL_CIRCLE",
                "EN",
                3,
                "STANDARD",
                1,
                true,
                true,
                true,
                true,
                now
        );
        userSettingsRepository.save(settings);

        System.out.println("User service 2");

        // Initialize weight
        WeightEntry firstWeightEntry = new WeightEntry();
        firstWeightEntry.setUserId(savedUser.getId());
        firstWeightEntry.setDate(now);
        firstWeightEntry.setWeight(registerRequest.getCurrentWeight());
        WeightEntry savedWeightEntry = weightRepository.save(firstWeightEntry);

        System.out.println("User service 3");

        // Initialize profile
        Profile profile = new Profile();
        profile.setUserId(savedUser.getId());
        profile.setHeight(registerRequest.getHeight());
        profile.setAge(registerRequest.getAge());
        profile.setSex(registerRequest.getSex());
        profile.setActivityLevel(registerRequest.getActivityLevel());
        profile.setUpdatedAt(now);
        Profile savedProfile = profileRepository.save(profile);

        System.out.println("User service 4");

        // Initialize user goals
        Goals goals = new Goals();
        goals.setUserId(savedUser.getId());
        goals.setGoalType(registerRequest.getGoalType());
        goals.setStartDate(today);
        goals.setTargetWeight(registerRequest.getTargetWeight());
        goals.setTargetDate(registerRequest.getTargetDate());
        goals.setStartWeight(registerRequest.getCurrentWeight());
        goals.setCreatedAt(now);
        goals.setUpdatedAt(now);
        goals.calculateNutrientTargets(savedProfile, savedWeightEntry.getWeight());
        goalsRepository.save(goals);

        System.out.println("User service 5");

        UserResponse userResponse = new UserResponse();
        String decryptedEmail = SecurityUtil.decrypt(savedUser.getEmail()); // Plain text email for the return object
        userResponse.setId(savedUser.getId());
        userResponse.setEmail(decryptedEmail);
        userResponse.setRole(savedUser.getRole());

        System.out.println("User service 6");

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<Page<UserResponse>> getAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<User> data = repository.findAll(pageRequest);

        // Decrypting user data
        Page<UserResponse> decryptedUsers = data.map(user -> {
            try {
                String decryptedEmail = SecurityUtil.decrypt(user.getEmail());
                user.setEmail(decryptedEmail);
                user.setPassword(null);

                return new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getRole()
                );
            } catch (FailedCryptionException | EncryptionKeyException e) {
                return null;
            }
        });

        return new ResponseEntity<>(decryptedUsers, HttpStatus.OK);
    }

    public ResponseEntity<UserResponse> getById(UUID id) throws FailedCryptionException, EncryptionKeyException {
        User user = repository.findById(id).orElseThrow(UserNotFoundException::new);

        user.setEmail(SecurityUtil.decrypt(user.getEmail()));
        user.setPassword(null);

        return new ResponseEntity<>(user.toResponse(), HttpStatus.OK);
    }

    public ResponseEntity<UserResponse> update(UUID id, UserUpdateRequest request) throws FailedCryptionException, EncryptionKeyException {
        User existing = repository.findById(id).orElseThrow(UserNotFoundException::new);

        existing.updateEntityFromRequest(request);
        User data = repository.save(existing);

        return new ResponseEntity<>(data.toResponse(), HttpStatus.OK);
    }

    public ResponseEntity<UserResponse> login(String email, String password) throws FailedCryptionException, EncryptionKeyException {
        String encryptedEmail = SecurityUtil.encrypt(email);
        User existingUser = repository.findByEmail(encryptedEmail).orElseThrow(UserNotFoundException::new);

        if (SecurityUtil.checkPassword(password, existingUser.getPassword())) {
            existingUser.setPassword(null);
            existingUser.setEmail(email);
            return new ResponseEntity<>(existingUser.toResponse(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<String> delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException();
        }

        repository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Boolean> isEmailTaken(String email) throws FailedCryptionException, EncryptionKeyException {
        String encryptedEmail = SecurityUtil.encrypt(email);
        User found = repository.findByEmail(encryptedEmail).orElse(null);

        if (found != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
