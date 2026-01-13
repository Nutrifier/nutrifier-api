package fi.nutrifier.dto;

import fi.nutrifier.entities.MealPlan;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateMealPlanRequest {
    private MealPlan mealPlan;
}
