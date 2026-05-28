package fi.nutrifier.services;

import fi.nutrifier.dto.AnalyticsFullResponse;
import fi.nutrifier.dto.AnalyticsSingleResponse;
import fi.nutrifier.dto.DailySummaryResponse;
import fi.nutrifier.entities.DailySummary;
import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.enums.AnalyticsTimePeriod;
import fi.nutrifier.enums.DayGoalResult;
import fi.nutrifier.exceptions.DailySummaryNotFoundException;
import fi.nutrifier.repositories.DailySummaryRepository;
import fi.nutrifier.repositories.FoodEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class DailySummaryService {

    private final DailySummaryRepository repository;
    private final FoodEntryRepository foodEntryRepository;
    private final DailySummaryRepository dailySummaryRepository;
    private final AnalyticsService analyticsService;

    @Autowired
    public DailySummaryService(
            DailySummaryRepository repository,
            FoodEntryRepository foodEntryRepository,
            DailySummaryRepository dailySummaryRepository,
            AnalyticsService analyticsService
    ) {
        this.repository = repository;
        this.foodEntryRepository = foodEntryRepository;
        this.dailySummaryRepository = dailySummaryRepository;
        this.analyticsService = analyticsService;
    }

    public ResponseEntity<DailySummaryResponse> create(LocalDate date, UUID userId) {
        List<FoodEntry> entries = foodEntryRepository.findByDateAndUserId(date, userId);

        DailySummaryResponse dailySummary = new DailySummaryResponse();
        entries.forEach(dailySummary::appendNutritionsFromEntry);

        return ResponseEntity.ok(dailySummary);
    }

    public ResponseEntity<DailySummaryResponse> getAndCalculateSummary(LocalDate date, UUID userId) {
        List<FoodEntry> entries = foodEntryRepository.findByDateAndUserId(date, userId);

        DailySummary dailySummary = dailySummaryRepository.findByDateAndUserId(date, userId);

        if (dailySummary == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        DailySummaryResponse dailySummaryResponse = dailySummary.toResponse();
        entries.forEach(dailySummaryResponse::appendNutritionsFromEntry);

        ResponseEntity<AnalyticsSingleResponse> analytics = analyticsService.calculateAnalyticsByDate(date, userId, dailySummary);
        if (analytics.getStatusCode() == HttpStatus.OK) {
            AnalyticsSingleResponse analyticsSingleResponse = analytics.getBody();
            if (analyticsSingleResponse != null && analyticsSingleResponse.getResult() == DayGoalResult.SUCCESS) {
                dailySummaryResponse.setConfirmed(true);

                dailySummary.setConfirmed(true);
                dailySummaryRepository.save(dailySummary);
            }
        }

        return ResponseEntity.ok(dailySummaryResponse);
    }

    public ResponseEntity<DailySummaryResponse> confirmDate(LocalDate date, UUID userId) {
        DailySummary dailySummary = dailySummaryRepository.findByDateAndUserId(date, userId);

        if (dailySummary == null) {
            throw new DailySummaryNotFoundException("Cannot confirm date when daily summary is not found");
        }

        dailySummary.setConfirmed(true);
        dailySummaryRepository.save(dailySummary);

        return ResponseEntity.ok(dailySummary.toResponse());
    }
}