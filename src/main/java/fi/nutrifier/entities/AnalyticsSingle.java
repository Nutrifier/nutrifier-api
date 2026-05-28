package fi.nutrifier.entities;

import fi.nutrifier.dto.AnalyticsSingleResponse;
import fi.nutrifier.enums.DayGoalResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalyticsSingle {
    private DayGoalResult result;
    private Double totalConsumedCalories;
    private Double totalGoalCalories;
    private Double totalCalorieBalance;

    public AnalyticsSingle() {
        this.result = DayGoalResult.MISSED;
        this.totalConsumedCalories = 0.0;
        this.totalGoalCalories = 0.0;
        this.totalCalorieBalance = 0.0;
    }

    public AnalyticsSingleResponse toResponse() {
        return new AnalyticsSingleResponse(
                this.result,
                this.totalConsumedCalories,
                this.totalGoalCalories,
                this.totalCalorieBalance
        );
    }

    public void appendCalories(double actual, double goal, DayGoalResult result) {
        this.totalConsumedCalories += actual;
        this.totalGoalCalories += goal;
        this.result = result;
    }

    public void calculateTotalCalorieBalance() {
        this.totalCalorieBalance = this.totalGoalCalories - this.totalConsumedCalories;
    }
}