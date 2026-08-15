package fi.nutrifier.entities;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("servingTypeId")
    @JoinColumn(name = "serving_type_id", nullable = false)
    private ServingType servingType;

    @Column(nullable = false)
    @NotNull
    @Min(value = 0)
    private Double amount;
}
