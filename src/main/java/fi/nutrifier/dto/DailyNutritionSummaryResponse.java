package fi.nutrifier.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class DailyNutritionSummaryResponse {

    @NotNull
    private UUID id;

    @NotNull
    private LocalDate date;

    @NotNull
    private double caloriesTarget;

    @NotNull
    private double fatTarget;

    @NotNull
    private double carbsTarget;

    @NotNull
    private double proteinTarget;

    @NotNull
    private double caloriesConsumed;

    @NotNull
    private double fatConsumed;

    @NotNull
    private double carbsConsumed;

    @NotNull
    private double proteinConsumed;
}