package fi.nutrifier.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FoodBarcodeRequest {

    @NotBlank
    private String barcode;
}