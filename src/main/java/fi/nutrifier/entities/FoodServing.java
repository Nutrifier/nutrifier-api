package fi.nutrifier.entities;

import fi.nutrifier.enums.ServingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "food_servings")
@NoArgsConstructor
@AllArgsConstructor
public class FoodServing {

    @EmbeddedId
    private FoodServingId id;

    private ServingType servingType;

    @Column(nullable = false)
    @NotNull
    @Min(value = 0)
    private Double amount;
}
