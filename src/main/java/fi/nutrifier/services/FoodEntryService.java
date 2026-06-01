package fi.nutrifier.services;

import fi.nutrifier.dto.FoodEntryRequest;
import fi.nutrifier.dto.FoodEntryResponse;
import fi.nutrifier.entities.*;
import fi.nutrifier.enums.MealType;
import fi.nutrifier.exceptions.FoodEntryNotFoundException;
import fi.nutrifier.exceptions.FoodNotFoundException;
import fi.nutrifier.exceptions.GoalsNotFoundException;
import fi.nutrifier.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FoodEntryService {

    private final FoodEntryRepository repository;
    private final FoodUsageService foodUsageService;

    private final FoodRepository foodRepository;
    private final DailySummaryRepository dailySummaryRepository;
    private final GoalsRepository goalsRepository;

    @Autowired
    public FoodEntryService(
            FoodEntryRepository repository,
            FoodUsageService foodUsageService,
            FoodRepository foodRepository,
            DailySummaryRepository dailySummaryRepository,
            GoalsRepository goalsRepository
    ) {
        this.repository = repository;
        this.foodUsageService = foodUsageService;
        this.foodRepository = foodRepository;
        this.dailySummaryRepository = dailySummaryRepository;
        this.goalsRepository = goalsRepository;
    }

    @Transactional
    public ResponseEntity<FoodEntryResponse> create(UUID userId, FoodEntryRequest request) {
        Food food = foodRepository.findById(request.getFoodId()).orElseThrow(FoodNotFoundException::new);

        // Saving snapshot values inside toEntity conversion method
        FoodEntry savableEntry = request.toEntity(
                userId,
                food.getCalories(),
                food.getFat(),
                food.getCarbs(),
                food.getProtein()
        );

        FoodEntry savedEntry = repository.save(savableEntry);
        foodUsageService.track(userId, savedEntry);

        Goals goals = goalsRepository.findByUserId(userId).orElseThrow(GoalsNotFoundException::new);
        DailySummary summary = dailySummaryRepository.findByDateAndUserId(savedEntry.getDate(), savedEntry.getUserId());

        if (summary == null) {
            summary = new DailySummary();
            summary.setUserId(savedEntry.getUserId());
            summary.setDate(savedEntry.getDate());
            summary.setCalorieTarget(goals.getDailyCalorieTarget());
            summary.setFatTarget(goals.getDailyFatTarget());
            summary.setCarbTarget(goals.getDailyCarbTarget());
            summary.setProteinTarget(goals.getDailyProteinTarget());
            summary.setConfirmed(false);
            dailySummaryRepository.save(summary);
        }

        return new ResponseEntity<>(savedEntry.toResponse(), HttpStatus.CREATED);
    }

    public ResponseEntity<Page<FoodEntryResponse>> getAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<FoodEntryResponse> dtoPage = repository.findAll(pageRequest).map(FoodEntry::toResponse);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    public ResponseEntity<Page<FoodEntryResponse>> getAllByUserId(UUID userId, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<FoodEntryResponse> dtoPage = repository.findByUserId(userId, pageRequest).map(FoodEntry::toResponse);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    public ResponseEntity<FoodEntryResponse> getById(UUID id) {
        FoodEntry data = repository.findById(id).orElseThrow(FoodEntryNotFoundException::new);
        return new ResponseEntity<>(data.toResponse(), HttpStatus.OK);
    }

    public ResponseEntity<FoodEntryResponse> getByIdAndUserId(UUID id, UUID userId) {
        FoodEntry data = repository.findByIdAndUserId(id, userId).orElseThrow(FoodEntryNotFoundException::new);
        return new ResponseEntity<>(data.toResponse(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<FoodEntryResponse> update(UUID userId, UUID id, FoodEntry entity) {
        FoodEntry existing = repository.findByIdAndUserId(id, userId).orElseThrow(FoodEntryNotFoundException::new);

        existing.updateEntityFromRequest(entity);
        FoodEntry data = repository.save(existing);

        return new ResponseEntity<>(data.toResponse(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> delete(UUID userId, UUID id) {
        repository.findByIdAndUserId(id, userId).orElseThrow(FoodEntryNotFoundException::new);

        repository.deleteByIdAndUserId(id, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<List<FoodEntryResponse>> getLogsByDateAndMealTypeAndUserId(LocalDate date, MealType mealType, UUID userId) {
        List<FoodEntry> found;

        if (mealType != null) {
            found = repository.findByDateAndMealTypeAndUserId(date, mealType, userId);
        } else {
            found = repository.findByDateAndUserId(date, userId);
        }

        List<FoodEntryResponse> mapped = found.stream().map(FoodEntry::toResponse).toList();

        return new ResponseEntity<>(mapped, HttpStatus.OK);
    }

    public ResponseEntity<FoodEntryResponse> recalculateSnapshots(UUID userId, UUID id) {
        FoodEntry foundEntry = repository.findByIdAndUserId(id, userId).orElseThrow(FoodEntryNotFoundException::new);
        Food foundFood = foodRepository.findById(foundEntry.getFoodId()).orElseThrow(FoodNotFoundException::new);

        foundEntry.recalculateSnapshotsFromFood(foundFood);
        FoodEntry updated = repository.save(foundEntry);

        return new ResponseEntity<>(updated.toResponse(), HttpStatus.OK);
    }
}