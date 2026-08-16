package fi.nutrifier.dto;

import fi.nutrifier.entities.ServingType;
import fi.nutrifier.enums.FoodStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FoodResponse extends ApiResponse {
    private UUID id;
    private String name;
    private UUID brandId;
    private UUID categoryId;
    private String barcode;
    private Double calories;
    private Double carbs;
    private Double protein;
    private Double fat;
    private Boolean verified;
    private FoodStatus status;
    private Map<ServingType, Double> servings;
}