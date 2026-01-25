package fi.nutrifier.services;

import fi.nutrifier.dto.UserGoalsUpdateRequest;
import fi.nutrifier.dto.UserSettingsUpdateRequest;
import fi.nutrifier.entities.UserGoals;
import fi.nutrifier.entities.UserSettings;
import fi.nutrifier.repositories.UserGoalsRepository;
import fi.nutrifier.repositories.UserRepository;
import fi.nutrifier.repositories.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserGoalsService {

    private final UserGoalsRepository goalsRepository;

    @Autowired
    public UserGoalsService(UserGoalsRepository goalsRepository) {
        this.goalsRepository = goalsRepository;
    }

    public ResponseEntity<UserGoals> getUserGoals(String userId) {
        try {
            UserGoals goals = goalsRepository.findByUserId(userId).orElse(null);

            if (goals == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(goals, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<UserGoals> update(String userId, UserGoalsUpdateRequest request) {
        try {
            UserGoals goals = goalsRepository.findByUserId(userId).orElse(null);

            if (goals == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            // Updating fields if new values given
            if (request.getReasoning() != null) {
                goals.setReasoning(request.getReasoning());
            }
            if (request.getTargetWeight() != null) {
                goals.setTargetWeight(request.getTargetWeight());
            }
            if (request.getTargetDate() != null) {
                goals.setTargetDate(request.getTargetDate());
            }
            if (request.getReachedDate() != null) {
                goals.setReachedDate(request.getReachedDate());
            }

            goals.setUpdatedAt(LocalDateTime.now());
            UserGoals updatedUserGoals = goalsRepository.save(goals);

            return new ResponseEntity<>(updatedUserGoals, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
