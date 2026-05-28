package fi.nutrifier.entities;

import fi.nutrifier.dto.AnalyticsFullResponse;
import fi.nutrifier.enums.DayGoalResult;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class AnalyticsFull {
    private LocalDate startOfPeriod;
    private LocalDate endOfPeriod;
    private Integer missedDayCount;
    private Integer successfulDayCount;
    private Integer failedDayCount;
    private Double totalConsumedCalories;
    private Double totalGoalCalories;
    private Double totalCalorieBalance;
    private Double avgCalorieBalance;
    private Map<LocalDate, DayGoalResult> dayResults;
    private Map<Month, AnalyticsData> monthCalories;
    private Map<Month, DayGoalResult> monthResults;

    public AnalyticsFull(LocalDate startDate, LocalDate endDate) {
        this.startOfPeriod = startDate;
        this.endOfPeriod = endDate;
        this.dayResults = new HashMap<>();
        this.monthCalories = new HashMap<>();
        this.monthResults = new HashMap<>();
        this.missedDayCount = 0;
        this.successfulDayCount = 0;
        this.failedDayCount = 0;
        this.totalConsumedCalories = 0.0;
        this.totalGoalCalories = 0.0;
        this.totalCalorieBalance = 0.0;
        this.avgCalorieBalance = 0.0;
    }

    public AnalyticsFullResponse toResponse() {
        return new AnalyticsFullResponse(
                this.startOfPeriod,
                this.endOfPeriod,
                this.missedDayCount,
                this.successfulDayCount,
                this.failedDayCount,
                this.totalConsumedCalories,
                this.totalGoalCalories,
                this.totalCalorieBalance,
                this.avgCalorieBalance,
                this.dayResults,
                this.monthResults
        );
    }

    public void appendCalories(LocalDate date, double actual, double goal) {
        this.totalConsumedCalories += actual;
        this.totalGoalCalories += goal;

        AnalyticsData initialMonthCalories = this.monthCalories.get(date.getMonth()) == null
                ? new AnalyticsData(0.0, 0.0)
                : this.monthCalories.get(date.getMonth());

        initialMonthCalories.appendActualAndGoal(actual, goal);
        this.monthCalories.put(date.getMonth(), initialMonthCalories);
    }

    public void appendMiss() {
        this.missedDayCount += 1;
    }

    public void appendSuccess(LocalDate date, double actualCalories, double goalCalories) {
        this.dayResults.put(date, DayGoalResult.SUCCESS);
        this.successfulDayCount += 1;
        appendCalories(date, actualCalories, goalCalories);
    }

    public void appendFail(LocalDate date, double actualCalories, double goalCalories) {
        this.dayResults.put(date, DayGoalResult.FAILED);
        this.failedDayCount += 1;
        appendCalories(date, actualCalories, goalCalories);
    }

    public void appendUncertain(LocalDate date) {
        this.dayResults.put(date, DayGoalResult.UNCERTAIN);
        // Not appending calorie goal and net values for uncertain days
        // TODO: Add functionality to check uncertain days as valid -> recalculation
    }

    public void calculateTotalCalorieBalance(double tdee) {
        int totalDayCount = this.successfulDayCount + this.failedDayCount;
        this.totalCalorieBalance = this.totalConsumedCalories - (tdee * totalDayCount);
    }

    public void calculateAvgCalorieBalance() {
        int totalDayCount = this.successfulDayCount + this.failedDayCount;
        if (totalDayCount > 0) this.avgCalorieBalance = this.totalCalorieBalance / totalDayCount;
    }

    public void calculateMonthCalories() {
        for (Month key : this.monthCalories.keySet()) {
            AnalyticsData value = this.monthCalories.get(key);

            double delta = value.getActual() - value.getGoal();

            if (Math.abs(delta) <= value.getGoal() * 0.1) {
                this.monthResults.put(key, DayGoalResult.SUCCESS);
            } else if (delta > 0) {
                this.monthResults.put(key, DayGoalResult.FAILED);
            } else {
                this.monthResults.put(key, DayGoalResult.FAILED);
            }
        }
    }
}