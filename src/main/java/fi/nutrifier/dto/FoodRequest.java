package fi.nutrifier.dto;

import fi.nutrifier.entities.Food;
import fi.nutrifier.enums.FoodStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FoodRequest {

    @NotBlank
    @NotNull
    private String name;

    private String brand;
    private String category;
    private String barcode;

    @Min(1)
    private Integer servingSize;

    @NotNull
    @Min(0)
    private Double calories;

    @NotNull
    @Min(0)
    private Double carbs;

    @NotNull
    @Min(0)
    private Double protein;

    @NotNull
    @Min(0)
    private Double fat;

    public Food toEntity(UUID userId) {
        LocalDateTime now = LocalDateTime.now();
        return new Food(
                UUID.randomUUID(),
                this.name,
                this.brand,
                this.category,
                this.barcode,
                this.servingSize,
                this.calories,
                this.carbs,
                this.protein,
                this.fat,
                false,
                FoodStatus.INACTIVE,
                userId,
                userId,
                now,
                now
        );
    }
}