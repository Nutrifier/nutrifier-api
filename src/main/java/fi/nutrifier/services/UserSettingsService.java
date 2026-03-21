package fi.nutrifier.services;

import fi.nutrifier.dto.UserSettingsUpdateRequest;
import fi.nutrifier.entities.Settings;
import fi.nutrifier.repositories.UserRepository;
import fi.nutrifier.repositories.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<Settings> get(UUID userId) {
        try {
            Settings settings = settingsRepository.findByUserId(userId).orElse(null);

            if (settings == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(settings, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Settings> update(UUID userId, UserSettingsUpdateRequest request) {
        try {
            System.out.println("Updating settings userId: " + userId + " request: " + request);
            Settings settings = settingsRepository.findByUserId(userId).orElse(null);

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
            Settings updatedSettings = settingsRepository.save(settings);

            return ResponseEntity.ok(updatedSettings);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
