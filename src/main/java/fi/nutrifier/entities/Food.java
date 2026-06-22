package fi.nutrifier.entities;

import fi.nutrifier.dto.FoodRequest;
import fi.nutrifier.dto.FoodResponse;
import fi.nutrifier.enums.FoodStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "foods")
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id; // Generating id in the mapper

    @Column(nullable = false)
    @NotBlank
    private String name;

    private String brand;
    private String category; // TODO: Enumerate
    private String barcode;

    @Column(nullable = false)
    @NotNull
    @Min(value = 0)
    private Double calories;

    @Min(value = 0)
    private Double carbs;

    @Min(value = 0)
    private Double protein;

    @Min(value = 0)
    private Double fat;

    private Boolean verified;

    @Enumerated(EnumType.STRING)
    private FoodStatus status;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)", nullable = false)
    private UUID createdBy;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)", nullable = false)
    private UUID updatedBy;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public FoodRequest toRequest() {
        return new FoodRequest(
                this.name,
                this.brand,
                this.category,
                this.barcode,
                this.calories,
                this.carbs,
                this.protein,
                this.fat,
                null
        );
    }

    public FoodRequest toRequest(List<FoodServing> servings) {
        return new FoodRequest(
                this.name,
                this.brand,
                this.category,
                this.barcode,
                this.calories,
                this.carbs,
                this.protein,
                this.fat,
                servings
        );
    }

    public FoodResponse toResponse() {
        return new FoodResponse(
                this.id,
                this.name,
                this.brand,
                this.category,
                this.barcode,
                this.calories,
                this.carbs,
                this.protein,
                this.fat,
                this.verified,
                this.status,
                null
        );
    }

    public FoodResponse toResponse(List<FoodServing> servings) {
        return new FoodResponse(
                this.id,
                this.name,
                this.brand,
                this.category,
                this.barcode,
                this.calories,
                this.carbs,
                this.protein,
                this.fat,
                this.verified,
                this.status,
                servings
        );
    }

    public void updateEntityFromRequest(FoodRequest request) {
        this.setName(request.getName());
        this.setBarcode(request.getBarcode());
        this.setCalories(request.getCalories());
        this.setCarbs(request.getCarbs());
        this.setProtein(request.getProtein());
        this.setFat(request.getFat());
        this.setUpdatedAt(LocalDateTime.now());
    }
}
