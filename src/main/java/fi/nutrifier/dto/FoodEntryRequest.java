package fi.nutrifier.dto;

import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.entities.MealType;
import fi.nutrifier.entities.ServingType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FoodEntryRequest {

    @Min(0)
    private Double servingAmount;
    private LocalDate date;
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @Enumerated(EnumType.STRING)
    private ServingType servingType;

    private Integer fineliId;
    private UUID foodId;

    public FoodEntry toEntity(UUID userId, Double calories, Double fat, Double carbs, Double protein) {
        return new FoodEntry(
                null,
                this.servingAmount,
                this.date,
                this.time,
                this.mealType,
                this.servingType,
                calories,
                fat,
                carbs,
                protein,
                this.fineliId,
                userId,
                this.foodId
        );
    }
}