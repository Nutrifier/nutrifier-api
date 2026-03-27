package fi.nutrifier.dto;

import fi.nutrifier.enums.FoodStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class FoodResponse {
    private UUID id;
    private String name;
    private String brand;
    private String category;
    private String barcode;
    private Integer servingSize;
    private Double calories;
    private Double carbs;
    private Double protein;
    private Double fat;
    private Boolean verified;
    private FoodStatus status;
}