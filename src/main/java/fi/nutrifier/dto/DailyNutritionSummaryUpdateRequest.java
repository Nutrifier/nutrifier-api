package fi.nutrifier.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyNutritionSummaryUpdateRequest {

    @NotNull
    private LocalDate date;

    @NotNull
    private double caloriesGoal;

    @NotNull
    private double fatGoal;

    @NotNull
    private double carbsGoal;

    @NotNull
    private double proteinGoal;

    @NotNull
    private double caloriesConsumed;

    @NotNull
    private double fatConsumed;

    @NotNull
    private double carbsConsumed;

    @NotNull
    private double proteinConsumed;
}