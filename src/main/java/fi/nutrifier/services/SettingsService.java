package fi.nutrifier.services;

import fi.nutrifier.dto.SettingsUpdateRequest;
import fi.nutrifier.entities.Settings;
import fi.nutrifier.exceptions.SettingsNotFoundException;
import fi.nutrifier.repositories.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SettingsService {

    private final UserSettingsRepository settingsRepository;

    @Autowired
    public SettingsService(UserSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public ResponseEntity<Settings> get(UUID userId) {
        Settings settings = settingsRepository.findByUserId(userId).orElseThrow(SettingsNotFoundException::new);
        return new ResponseEntity<>(settings, HttpStatus.OK);
    }

    public ResponseEntity<Settings> update(UUID userId, SettingsUpdateRequest request) {
        Settings existing = settingsRepository.findByUserId(userId).orElseThrow(SettingsNotFoundException::new);

        existing.updateEntityFromRequest(request);
        Settings updatedSettings = settingsRepository.save(existing);

        return ResponseEntity.ok(updatedSettings);
    }
}
