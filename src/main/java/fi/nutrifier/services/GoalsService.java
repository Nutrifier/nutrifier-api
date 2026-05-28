package fi.nutrifier.services;

import fi.nutrifier.dto.GoalsResponse;
import fi.nutrifier.dto.GoalsUpdateRequest;
import fi.nutrifier.entities.*;
import fi.nutrifier.exceptions.GoalsNotFoundException;
import fi.nutrifier.exceptions.ProfileNotFoundException;
import fi.nutrifier.exceptions.WeightEntryNotFoundException;
import fi.nutrifier.repositories.GoalsRepository;
import fi.nutrifier.repositories.ProfileRepository;
import fi.nutrifier.repositories.WeightRepository;
import fi.nutrifier.utils.CalculationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GoalsService {

    private final GoalsRepository goalsRepository;
    private final ProfileRepository profileRepository;
    private final WeightRepository weightRepository;

    @Autowired
    public GoalsService(
            GoalsRepository goalsRepository,
            ProfileRepository profileRepository,
            WeightRepository weightRepository
    ) {
        this.goalsRepository = goalsRepository;
        this.profileRepository = profileRepository;
        this.weightRepository = weightRepository;
    }

    public ResponseEntity<GoalsResponse> getUserGoals(UUID userId) {
        Goals goals = goalsRepository.findByUserId(userId).orElseThrow(GoalsNotFoundException::new);
        return new ResponseEntity<>(goals.toResponse(), HttpStatus.OK);
    }

    public ResponseEntity<GoalsResponse> update(UUID userId, GoalsUpdateRequest request) {
        Goals goals = goalsRepository.findByUserId(userId).orElseThrow(GoalsNotFoundException::new);

        goals.updateEntityFromRequest(request);
        Goals updatedGoals = goalsRepository.save(goals);

        return new ResponseEntity<>(updatedGoals.toResponse(), HttpStatus.OK);
    }

    public ResponseEntity<GoalsResponse> recalculateGoals(UUID userId) {
        Profile profile = profileRepository.findByUserId(userId).orElseThrow(ProfileNotFoundException::new);
        Goals goals = goalsRepository.findByUserId(userId).orElseThrow(GoalsNotFoundException::new);

        WeightEntry weightEntry = weightRepository
                .findByUserIdOrderByDateDesc(userId, Pageable.ofSize(10))
                .orElseThrow(WeightEntryNotFoundException::new)
                .getContent()
                .getFirst();

        goals.calculateNutrientTargets(profile, weightEntry.getWeight());

        return new ResponseEntity<>(goals.toResponse(), HttpStatus.OK);
    }
}
