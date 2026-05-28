package fi.nutrifier.services;

import fi.nutrifier.dto.ProfileResponse;
import fi.nutrifier.dto.ProfileUpdateRequest;
import fi.nutrifier.entities.Profile;
import fi.nutrifier.exceptions.ProfileNotFoundException;
import fi.nutrifier.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileService {

    private final ProfileRepository repository;

    @Autowired
    public ProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<ProfileResponse> getProfile(UUID userId) {
        Profile profile = repository.findByUserId(userId).orElse(null);

        if (profile == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(profile.toResponse(), HttpStatus.OK);
    }

    public ResponseEntity<ProfileResponse> update(UUID userId, ProfileUpdateRequest request) {
        Profile existing = repository.findByUserId(userId).orElseThrow(ProfileNotFoundException::new);

        existing.updateEntityFromRequest(request);
        Profile updated = repository.save(existing);

        return new ResponseEntity<>(updated.toResponse(), HttpStatus.OK);
    }
}
