package fi.nutrifier.dto;

import fi.nutrifier.enums.DayGoalResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalyticsSingleResponse {
    private DayGoalResult result;
    private Double totalConsumedCalories;
    private Double totalGoalCalories;
    private Double totalCalorieBalance;
}