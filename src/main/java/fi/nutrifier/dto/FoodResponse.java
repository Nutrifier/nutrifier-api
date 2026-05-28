package fi.nutrifier.dto;

import fi.nutrifier.enums.FoodStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class FoodResponse extends ApiResponse {
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