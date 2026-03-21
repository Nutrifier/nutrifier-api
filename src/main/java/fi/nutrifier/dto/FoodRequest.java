package fi.nutrifier.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FoodRequest {

    @NotBlank
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
}