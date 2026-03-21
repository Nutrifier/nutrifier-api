package fi.nutrifier.dto;

import fi.nutrifier.entities.RecipeIngredientSection;
import fi.nutrifier.entities.RecipeStep;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

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
}