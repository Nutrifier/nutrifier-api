package fi.nutrifier.services;

import fi.nutrifier.dto.AuthRequest;
import fi.nutrifier.dto.UserDto;
import fi.nutrifier.dto.UserSettingsUpdateRequest;
import fi.nutrifier.entities.Role;
import fi.nutrifier.entities.User;
import fi.nutrifier.entities.UserGoals;
import fi.nutrifier.entities.UserSettings;
import fi.nutrifier.exceptions.EncryptionKeyException;
import fi.nutrifier.exceptions.FailedCryptionException;
import fi.nutrifier.exceptions.FailedDecryptionException;
import fi.nutrifier.exceptions.FailedEncryptionException;
import fi.nutrifier.repositories.UserRepository;
import fi.nutrifier.repositories.UserSettingsRepository;
import fi.nutrifier.utils.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserSettingsService {

    private final UserRepository userRepository;
    private final UserSettingsRepository settingsRepository;

    @Autowired
    public UserSettingsService(UserRepository userRepository, UserSettingsRepository settingsRepository) {
        this.userRepository = userRepository;
        this.settingsRepository = settingsRepository;
    }

    public ResponseEntity<UserSettings> get(String userId) {
        try {
            UserSettings settings = settingsRepository.findByUserId(userId).orElse(null);

            if (settings == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(settings, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<UserSettings> update(String userId, UserSettingsUpdateRequest request) {
        try {
            System.out.println("Updating settings userId: " + userId + " request: " + request);
            UserSettings settings = settingsRepository.findByUserId(userId).orElse(null);

            System.out.println("Previous settings: " + settings);

            if (settings == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            // Updating fields if new values given
            if (request.getWeightUnit() != null) {
                settings.setWeightUnit(request.getWeightUnit());
            }
            if (request.getMacroWeightUnit() != null) {
                settings.setMacroWeightUnit(request.getMacroWeightUnit());
            }
            if (request.getEnergyUnit() != null) {
                settings.setEnergyUnit(request.getEnergyUnit());
            }
            if (request.getNutrientDisplayMode() != null) {
                settings.setNutrientDisplayMode(request.getNutrientDisplayMode());
            }
            if (request.getLanguage() != null) {
                settings.setLanguage(request.getLanguage());
            }
            if (request.getTimeBetweenMeals() != null) {
                settings.setTimeBetweenMeals(request.getTimeBetweenMeals());
            }
            if (request.getDiet() != null) {
                settings.setDiet(request.getDiet());
            }
            if (request.getWeekStartsOn() != null) {
                settings.setWeekStartsOn(request.getWeekStartsOn());
            }
            if (request.getProteinEfficiencyEnabled() != null) {
                settings.setProteinEfficiencyEnabled(request.getProteinEfficiencyEnabled());
            }
            if (request.getMealReminderEnabled() != null) {
                settings.setMealReminderEnabled(request.getMealReminderEnabled());
            }
            if (request.getWeighInReminderEnabled() != null) {
                settings.setWeighInReminderEnabled(request.getWeighInReminderEnabled());
            }
            if (request.getMotivationMessagesEnabled() != null) {
                settings.setMotivationMessagesEnabled(request.getMotivationMessagesEnabled());
            }

            settings.setUpdatedAt(LocalDateTime.now());
            UserSettings updatedSettings = settingsRepository.save(settings);

            return ResponseEntity.ok(updatedSettings);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
