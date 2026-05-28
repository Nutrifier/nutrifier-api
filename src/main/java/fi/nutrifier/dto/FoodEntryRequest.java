package fi.nutrifier.dto;

import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.enums.FoodWeightUnit;
import fi.nutrifier.enums.MealType;
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
    private Double amount;
    private LocalDate date;
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @Enumerated(EnumType.STRING)
    private FoodWeightUnit unit;

    private Integer fineliId;
    private UUID foodId;

    public FoodEntry toEntity(UUID userId, Double calories, Double fat, Double carbs, Double protein) {
        return new FoodEntry(
                null,
                this.amount,
                this.date,
                this.time,
                this.mealType,
                this.unit,
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