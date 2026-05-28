package fi.nutrifier.dto;

import fi.nutrifier.enums.DayGoalResult;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

@Data
@AllArgsConstructor
public class AnalyticsFullResponse {
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
    private Map<Month, DayGoalResult> monthResults;
}