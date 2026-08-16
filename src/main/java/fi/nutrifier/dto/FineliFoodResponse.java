package fi.nutrifier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

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
}