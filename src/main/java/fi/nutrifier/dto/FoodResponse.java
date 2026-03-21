package fi.nutrifier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class FoodResponse {
    private UUID id;
    private String name;
    private String barcode;
    private Integer servingSize = 100;
    private Double calories;
    private Double carbs;
    private Double protein;
    private Double fat;
}