package fi.nutrifier.services;

import fi.nutrifier.dto.RegisterRequest;
import fi.nutrifier.dto.UserResponse;
import fi.nutrifier.entities.*;
import fi.nutrifier.enums.Role;
import fi.nutrifier.exceptions.EncryptionKeyException;
import fi.nutrifier.exceptions.FailedCryptionException;
import fi.nutrifier.exceptions.FailedDecryptionException;
import fi.nutrifier.exceptions.FailedEncryptionException;
import fi.nutrifier.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import fi.nutrifier.utils.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserSettingsRepository userSettingsRepository;
    private final ProfileRepository profileRepository;
    private final GoalsRepository goalsRepository;
    private final WeightRepository weightRepository;

    @Autowired
    public UserService(
            UserRepository repository,
            UserSettingsRepository userSettingsRepository,
            ProfileRepository profileRepository,
            GoalsRepository goalsRepository,
            WeightRepository weightRepository
    ) {
        this.repository = repository;
        this.userSettingsRepository = userSettingsRepository;
        this.profileRepository = profileRepository;
        this.goalsRepository = goalsRepository;
        this.weightRepository = weightRepository;
    }

    @Transactional
    public ResponseEntity<UserResponse> create(RegisterRequest registerRequest) {
        try {
            String encryptedEmail = SecurityUtil.encrypt(registerRequest.getEmail());
            String hashedPassword = SecurityUtil.hashPassword(registerRequest.getPassword());

            User user = new User();
            user.setEmail(encryptedEmail);
            user.setPassword(hashedPassword);
            user.setRole(Role.REGULAR); // Default to regular user
            User savedUser = repository.save(user);

            System.out.println("1 user saved" + savedUser);

            // Initialize user settings
            Settings settings = new Settings(
                    savedUser.getId(),
                    "G",
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
                    LocalDateTime.now()
            );
            userSettingsRepository.save(settings);

            System.out.println("2 settings saved" + settings);

            // Initialize user goals
            Goals goals = new Goals();
            goals.setUserId(savedUser.getId());
            goals.setGoalType(registerRequest.getGoalType());
            goals.setTargetWeight(registerRequest.getTargetWeight());
            goals.setTargetDate(registerRequest.getTargetDate());
            goals.setCreatedAt(LocalDateTime.now());
            goals.setUpdatedAt(LocalDateTime.now());
            goalsRepository.save(goals);

            System.out.println("3 goals saved" + goals);

            // Initialize weight
            WeightEntry firstWeightEntry = new WeightEntry();
            firstWeightEntry.setUser(savedUser);
            firstWeightEntry.setDate(LocalDateTime.now());
            firstWeightEntry.setWeight(registerRequest.getCurrentWeight());
            weightRepository.save(firstWeightEntry);

            System.out.println("4 weight saved" + firstWeightEntry);

            // Initialize profile
            UserProfile profile = new UserProfile();
            profile.setUserId(savedUser.getId());
            profile.setSex(registerRequest.getSex());
            profile.setAge(registerRequest.getAge());
            profile.setHeight(registerRequest.getHeight());
            profile.setActivityLevel(registerRequest.getActivityLevel());
            profile.setUpdatedAt(LocalDateTime.now());
            profileRepository.save(profile);

            System.out.println("5 profile saved" + profile);

            UserResponse userResponse = new UserResponse();
            String decryptedEmail = SecurityUtil.decrypt(savedUser.getEmail()); // Plain text email for the return object
            userResponse.setId(savedUser.getId());
            userResponse.setEmail(decryptedEmail);
            userResponse.setRole(savedUser.getRole());

            System.out.println("6 response generated" + userResponse);

            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<UserResponse>> getAll(Integer page, Integer size) {
        try {
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
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<User> getById(UUID id) {
        try {
            User user = repository.findById(id).orElse(null);

            if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            try {
                user.setEmail(SecurityUtil.decrypt(user.getEmail()));
                user.setPassword(null);
                return new ResponseEntity<>(user, HttpStatus.OK);
            } catch (FailedDecryptionException e) {
                System.out.println("here: " + e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<User> update(UUID id, UserResponse userResponse) {
        try {
            User existingUser = repository.findById(id).orElse(null);

            if (existingUser == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            try {
                userResponse.setEmail(SecurityUtil.encrypt(userResponse.getEmail()));
                User data = repository.save(userResponse.toUser());
                data.setPassword(null);
                return new ResponseEntity<>(data, HttpStatus.OK);
            } catch (FailedEncryptionException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<User> login(String email, String password) {
        try {
            String encryptedEmail = SecurityUtil.encrypt(email);
            User existingUser = repository.findByEmail(encryptedEmail).orElse(null);

            if (existingUser == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            if (SecurityUtil.checkPassword(password, existingUser.getPassword())) {
                existingUser.setPassword(null);
                existingUser.setEmail(email);
                return new ResponseEntity<>(existingUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            System.out.println("login: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<User> delete(UUID id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Boolean> isEmailTaken(String email) {
        try {
            String encryptedEmail = SecurityUtil.encrypt(email);
            User found = repository.findByEmail(encryptedEmail).orElse(null);
            if (found != null) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
