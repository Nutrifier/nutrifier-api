package fi.nutrifier.services;

import fi.nutrifier.dto.ProfileResponse;
import fi.nutrifier.dto.ProfileUpdateRequest;
import fi.nutrifier.entities.Profile;
import fi.nutrifier.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProfileService {

    private final ProfileRepository repository;

    @Autowired
    public ProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<ProfileResponse> getProfile(UUID userId) {
        try {
            Profile profile = repository.findByUserId(userId).orElse(null);

            if (profile == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(profile.toResponse(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ProfileResponse> update(UUID userId, ProfileUpdateRequest request) {
        try {
            Profile profile = repository.findByUserId(userId).orElse(null);

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
            Profile updated = repository.save(profile);

            return new ResponseEntity<>(updated.toResponse(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
