package fi.nutrifier.dto;

import fi.nutrifier.enums.FoodWeightUnit;
import fi.nutrifier.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class FoodEntryResponse extends ApiResponse {
    private UUID id;
    private Double amount;
    private LocalDate date;
    private LocalTime time;
    private MealType mealType;
    private FoodWeightUnit unit;
    private Double caloriesSnapshot;
    private Double fatSnapshot;
    private Double carbsSnapshot;
    private Double proteinSnapshot;
    private Integer fineliId;
    private UUID userId;
    private UUID foodId;
}