package fi.nutrifier.entities;

import fi.nutrifier.dto.RecipeRequest;
import fi.nutrifier.dto.RecipeResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "recipes")
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id; // Generating id in the mapper

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "owner_id", columnDefinition = "CHAR(36)")
    private UUID userId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private int servings;

    private int preparationTime;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(nullable = false)
    private Boolean isForked;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeStep> steps = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredientSection> ingredientSections = new ArrayList<>();

    public RecipeResponse toResponse() {
        return new RecipeResponse(
                this.id,
                this.userId,
                this.name,
                this.description,
                this.servings,
                this.preparationTime,
                this.isPublic,
                this.isForked,
                this.steps,
                this.ingredientSections
        );
    }

    public void updateEntityFromRequest(RecipeRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.servings = request.getServings();
        this.preparationTime = request.getPreparationTime();
        this.isPublic = request.getIsPublic();
        this.isForked = request.getIsForked();
        this.updatedAt = LocalDateTime.now();
        this.steps = request.getSteps();
        this.ingredientSections = request.getIngredientSections();
    }
}
