package fi.nutrifier.dto;

import fi.nutrifier.entities.Food;
import fi.nutrifier.entities.FoodBrand;
import fi.nutrifier.enums.FoodStatus;
import fi.nutrifier.utils.Constants;
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
                FoodBrand.FINELI,
                null, // TODO: Check if Fineli returns a category and use that
                null, // No barcodes
                this.energyKcal,
                this.carbohydrate,
                this.protein,
                this.fat,
                true,
                FoodStatus.ACTIVE,
                Constants.FINELI_UUID, // FINELI_UUID
                Constants.FINELI_UUID,
                now,
                now
        );
    }
}