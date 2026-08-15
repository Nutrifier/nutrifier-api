package fi.nutrifier.dto;

import fi.nutrifier.entities.Food;
import fi.nutrifier.enums.FoodStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FineliFoodResponse {
    private Integer id;
    private FineliNameResponse name;
    private Double salt;
    private Double energyKcal;
    private Double energy;
    private Double fat;
    private Double protein;
    private Double carbohydrate;
    private Double alcohol;
    private Double organicAcids;
    private Double sugarAlcohol;
    private Double saturatedFat;
    private Double fiber;
    private Double sugar;

    public Food toDatabaseFood() {
        LocalDateTime now = LocalDateTime.now();
        return new Food(
                null,
                this.name.getFi(), // TODO: Localize
                "Fineli",
                null, // TODO: Check if Fineli returns a category and use that
                null, // No barcodes
                this.energyKcal,
                this.carbohydrate,
                this.protein,
                this.fat,
                true,
                FoodStatus.ACTIVE,
                UUID.fromString ("79fbbe73-6a12-498e-a051-8490122cb99c"), // FINELI_UUID
                UUID.fromString ("79fbbe73-6a12-498e-a051-8490122cb99c"),
                now,
                now
        );
    }
}