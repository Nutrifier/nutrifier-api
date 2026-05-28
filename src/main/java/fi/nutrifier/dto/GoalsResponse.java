package fi.nutrifier.dto;

import fi.nutrifier.enums.GoalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class GoalsResponse extends ApiResponse {
    private UUID id;
    private GoalType goalType;
    private LocalDate startDate;
    private LocalDate targetDate;
    private Double startWeight;
    private Double targetWeight;
    private Boolean isReached;
    private Double dailyTDEE;
    private Double dailyCalorieBalance;
    private Double dailyCalorieTarget;
    private Double dailyFatTarget;
    private Double dailyCarbTarget;
    private Double dailyProteinTarget;
}
