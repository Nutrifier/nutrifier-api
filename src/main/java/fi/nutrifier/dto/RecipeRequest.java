package fi.nutrifier.dto;

import fi.nutrifier.entities.Recipe;
import fi.nutrifier.entities.RecipeIngredientSection;
import fi.nutrifier.entities.RecipeStep;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RecipeRequest {
    private String name;
    private String description;
    private Integer servings;
    private Integer preparationTime;
    private Boolean isPublic;
    private Boolean isForked;
    private List<RecipeStep> steps;
    private List<RecipeIngredientSection> ingredientSections;

    public Recipe toEntity(UUID userId) {
        LocalDateTime now = LocalDateTime.now();
        return new Recipe(
                UUID.randomUUID(),
                userId,
                this.getName(),
                this.getDescription(),
                this.getServings(),
                this.getPreparationTime(),
                this.getIsPublic(),
                this.getIsForked(),
                now,
                now,
                this.getSteps(),
                this.getIngredientSections()
        );
    }
}