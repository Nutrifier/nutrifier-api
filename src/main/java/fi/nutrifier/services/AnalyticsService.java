package fi.nutrifier.services;

import fi.nutrifier.dto.AnalyticsFullResponse;
import fi.nutrifier.dto.AnalyticsSingleResponse;
import fi.nutrifier.entities.AnalyticsFull;
import fi.nutrifier.entities.*;
import fi.nutrifier.enums.AnalyticsTimePeriod;
import fi.nutrifier.enums.DayGoalResult;
import fi.nutrifier.exceptions.GoalsNotFoundException;
import fi.nutrifier.repositories.DailySummaryRepository;
import fi.nutrifier.repositories.FoodEntryRepository;
import fi.nutrifier.repositories.GoalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

@Service
public class AnalyticsService {

    private final FoodEntryRepository foodEntryRepository;
    private final GoalsRepository goalsRepository;
    private final DailySummaryRepository dailySummaryRepository;

    @Autowired
    public AnalyticsService(
            FoodEntryRepository foodEntryRepository,
            GoalsRepository goalsRepository,
            DailySummaryRepository dailySummaryRepository
    ) {
        this.foodEntryRepository = foodEntryRepository;
        this.goalsRepository = goalsRepository;
        this.dailySummaryRepository = dailySummaryRepository;
    }

    public ResponseEntity<AnalyticsSingleResponse> calculateAnalyticsByDate(
            LocalDate date,
            UUID userId,
            DailySummary dailySummary
    ) {
        List<FoodEntry> entries = foodEntryRepository.findByDateAndUserId(date, userId);
        Goals goals = goalsRepository.findByUserId(userId).orElseThrow(GoalsNotFoundException::new);

        AnalyticsSingle analytics = new AnalyticsSingle();

        double dailyCalories = 0.0;
        for (FoodEntry e : entries) {
            dailyCalories += e.getCaloriesSnapshot() * (e.getAmount() / 100);
        }

        // Getting dailySummary for the iterating day, so that can be used for the success/fail calculations
        double calorieTargetToUse = dailySummary != null
                ? dailySummary.getCalorieTarget()
                : goals.getDailyCalorieTarget();

        if (dailyCalories > calorieTargetToUse) {
            analytics.appendCalories(dailyCalories, calorieTargetToUse, DayGoalResult.FAILED);
        } else if (dailyCalories < (calorieTargetToUse * 0.8) && dailyCalories > 0.0) {
            if (dailySummary != null && dailySummary.getConfirmed()) {
                // Outcome is marked as success if the dailySummary is separately marked as confirmed
                analytics.appendCalories(dailyCalories, calorieTargetToUse, DayGoalResult.SUCCESS);
            } else {
                // Outcome is uncertain when daily calories are under 80% of the calorie target, user might not have logged everything
                analytics.appendCalories(dailyCalories, calorieTargetToUse, DayGoalResult.UNCERTAIN);
            }
        } else if (
                dailyCalories >= (calorieTargetToUse * 0.8)
                        && dailyCalories <= calorieTargetToUse
        )  {
            analytics.appendCalories(dailyCalories, calorieTargetToUse, DayGoalResult.SUCCESS);
        } else {
            analytics.appendCalories(dailyCalories, calorieTargetToUse, DayGoalResult.MISSED);
        }

        analytics.calculateTotalCalorieBalance();

        return ResponseEntity.ok(analytics.toResponse());
    }

    public ResponseEntity<AnalyticsFullResponse> calculateAnalyticsWithinDateRange(
            LocalDate date,
            UUID userId,
            AnalyticsTimePeriod period
    ) {
        LocalDate startDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        switch (period) {
            case MONTH -> {
                startDate = date.withDayOfMonth(1);
                endDate = date.withDayOfMonth(date.lengthOfMonth());
            }
            case YEAR -> {
                startDate = date.withDayOfYear(1);
                endDate = date.withDayOfYear(date.lengthOfYear());
            }
        }

        List<FoodEntry> entries = foodEntryRepository.findByDateBetweenAndUserId(startDate, endDate, userId);
        Goals goals = goalsRepository.findByUserId(userId).orElseThrow(GoalsNotFoundException::new);

        AnalyticsFull analyticsFull = new AnalyticsFull(startDate, endDate);

        for (LocalDate x = startDate; x.isBefore(endDate.plusDays(1)); x = x.plusDays(1)) {
            LocalDate finalX = x;
            List<FoodEntry> dailyEntries = entries.stream().filter(e -> e.getDate().isEqual(finalX)).toList();


            double dailyCalories = 0.0;
            for (FoodEntry e : dailyEntries) {
                dailyCalories += e.getCaloriesSnapshot() * (e.getAmount() / 100);
            }

            // Ignoring if calorieTarget is not set
            if (goals.getDailyCalorieTarget() <= 0.0) continue;

            // Getting dailySummary for the iterating day, so that can be used for the success/fail calculations
            DailySummary dailySummary = dailySummaryRepository.findByDateAndUserId(finalX, userId);
            double calorieTargetToUse = dailySummary != null
                    ? dailySummary.getCalorieTarget()
                    : goals.getDailyCalorieTarget();

            if (dailyCalories > calorieTargetToUse) {
                analyticsFull.appendFail(finalX, dailyCalories, calorieTargetToUse);
            } else if (dailyCalories < (calorieTargetToUse * 0.8) && dailyCalories > 0.0) {
                if (dailySummary != null && dailySummary.getConfirmed()) {
                    // Outcome is marked as success if the dailySummary is separately marked as confirmed
                    analyticsFull.appendSuccess(finalX, dailyCalories, calorieTargetToUse);
                } else {
                    // Outcome is uncertain when daily calories are under 80% of the calorie target, user might not have logged everything
                    analyticsFull.appendUncertain(finalX);
                }
            } else if (
                    dailyCalories >= (calorieTargetToUse * 0.8)
                    && dailyCalories <= calorieTargetToUse
            )  {
                analyticsFull.appendSuccess(finalX, dailyCalories, calorieTargetToUse);
            } else {
                analyticsFull.appendMiss();
            }
        }

        analyticsFull.calculateTotalCalorieBalance(goals.getDailyTDEE());
        analyticsFull.calculateAvgCalorieBalance();
        analyticsFull.calculateMonthCalories();

        return ResponseEntity.ok(analyticsFull.toResponse());
    }
}