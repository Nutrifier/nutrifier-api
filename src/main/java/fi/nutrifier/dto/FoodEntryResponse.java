package fi.nutrifier.dto;

import fi.nutrifier.entities.MealType;
import fi.nutrifier.entities.ServingType;
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
    private Double servingAmount;
    private LocalDate date;
    private LocalTime time;
    private MealType mealType;
    private ServingType servingType;
    private Double caloriesSnapshot;
    private Double fatSnapshot;
    private Double carbsSnapshot;
    private Double proteinSnapshot;
    private Integer fineliId;
    private UUID userId;
    private UUID foodId;
}