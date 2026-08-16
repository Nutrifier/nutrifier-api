package fi.nutrifier.dto;

import fi.nutrifier.entities.*;
import fi.nutrifier.enums.FoodStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FoodRequest {

    @NotBlank
    @NotNull
    private String name;

    private UUID brandId;
    private UUID categoryId;

    private String barcode;

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

    private List<FoodServing> servings;

    public Food toEntity(UUID userId, FoodBrand brand, FoodCategory category) {
        LocalDateTime now = LocalDateTime.now();
        return new Food(
                UUID.randomUUID(),
                this.name,
                brand,
                category,
                this.barcode,
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