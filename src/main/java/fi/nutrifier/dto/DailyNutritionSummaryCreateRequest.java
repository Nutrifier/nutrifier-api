package fi.nutrifier.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyNutritionSummaryCreateRequest {

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
}