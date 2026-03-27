package fi.nutrifier.dto;

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
    private String mealType;
    private String unit;
    private Double caloriesSnapshot;
    private Double fatSnapshot;
    private Double carbsSnapshot;
    private Double proteinSnapshot;
    private Integer fineliId;
    private UUID foodId;
}