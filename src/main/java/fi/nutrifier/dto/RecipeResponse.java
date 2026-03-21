package fi.nutrifier.dto;

import fi.nutrifier.entities.RecipeIngredientSection;
import fi.nutrifier.entities.RecipeStep;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RecipeResponse {
    private UUID id;
    private UUID userId;
    private String name;
    private String description;
    private Integer servings;
    private Integer preparationTime;
    private Boolean isPublic;
    private Boolean isForked;
    private List<RecipeStep> steps;
    private List<RecipeIngredientSection> ingredientSections;
}