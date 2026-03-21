package fi.nutrifier.services;

import fi.nutrifier.dto.UserProfileUpdateRequest;
import fi.nutrifier.entities.UserProfile;
import fi.nutrifier.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserProfileService {

    private final ProfileRepository repository;

    @Autowired
    public UserProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<UserProfile> getUserGoals(UUID userId) {
        try {
            UserProfile profile = repository.findByUserId(userId).orElse(null);

            if (profile == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(profile, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<UserProfile> update(UUID userId, UserProfileUpdateRequest request) {
        try {
            UserProfile profile = repository.findByUserId(userId).orElse(null);

            if (profile == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            // Updating fields if new values given
            if (request.getSex() != null) {
                profile.setSex(request.getSex());
            }
            if (request.getAge() != null) {
                profile.setAge(request.getAge());
            }
            if (request.getHeight() != null) {
                profile.setHeight(request.getHeight());
            }
            if (request.getActivityLevel() != null) {
                profile.setActivityLevel(request.getActivityLevel());
            }

            profile.setUpdatedAt(LocalDateTime.now());
            UserProfile updated = repository.save(profile);

            return new ResponseEntity<>(updated, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
