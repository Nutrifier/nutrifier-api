package fi.nutrifier.dto;

import fi.nutrifier.entities.DailySummaryMeal;
import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DailySummaryResponse {
    private double calorieTarget;
    private double fatTarget;
    private double carbTarget;
    private double proteinTarget;
    private Boolean confirmed;
    private double caloriesConsumed;
    private double fatConsumed;
    private double carbsConsumed;
    private double proteinConsumed;
    private Map<MealType, DailyMealSummary> mealSummaries;

    public DailySummaryResponse() {
        this.confirmed = false;
        this.caloriesConsumed = 0.0;
        this.fatConsumed = 0.0;
        this.carbsConsumed = 0.0;
        this.proteinConsumed = 0.0;
        this.mealSummaries = new HashMap<>();
        initializeMealSummaries();
    }

    public DailySummaryResponse(double calories, double fat, double carbs, double protein) {
        this.confirmed = false;
        this.caloriesConsumed = calories;
        this.fatConsumed = fat;
        this.carbsConsumed = carbs;
        this.proteinConsumed = protein;
        this.mealSummaries = new HashMap<>();
        initializeMealSummaries();
    }

    public DailySummaryResponse(
            double calorieTarget,
            double fatTarget,
            double carbTarget,
            double proteinTarget,
            Boolean confirmed,
            double caloriesConsumed,
            double fatConsumed,
            double carbsConsumed,
            double proteinConsumed
    ) {
        this.calorieTarget = calorieTarget;
        this.fatTarget = fatTarget;
        this.carbTarget = carbTarget;
        this.proteinTarget = proteinTarget;
        this.confirmed = confirmed;
        this.caloriesConsumed = caloriesConsumed;
        this.fatConsumed = fatConsumed;
        this.carbsConsumed = carbsConsumed;
        this.proteinConsumed = proteinConsumed;
        this.mealSummaries = new HashMap<>();
        initializeMealSummaries();
    }

    public void appendNutritionsFromEntry(FoodEntry entry) {
        this.caloriesConsumed += entry.getCaloriesSnapshot() * (entry.getAmount() / 100);
        this.fatConsumed += entry.getFatSnapshot() * (entry.getAmount() / 100);
        this.carbsConsumed += entry.getCarbsSnapshot() * (entry.getAmount() / 100);
        this.proteinConsumed += entry.getProteinSnapshot() * (entry.getAmount() / 100);

        DailyMealSummary dailyMealSummary = this.mealSummaries.get(entry.getMealType());
        dailyMealSummary.appendNutritionFromEntry(entry);

        System.out.println("dailyMealSummary: " + dailyMealSummary);

        this.mealSummaries.put(entry.getMealType(), dailyMealSummary);
    }

    private void initializeMealSummaries() {
        this.mealSummaries.put(MealType.BREAKFAST, new DailyMealSummary(0.0, 0.0, 0.0, 0.0));
        this.mealSummaries.put(MealType.LUNCH, new DailyMealSummary(0.0, 0.0, 0.0, 0.0));
        this.mealSummaries.put(MealType.DINNER, new DailyMealSummary(0.0, 0.0, 0.0, 0.0));
        this.mealSummaries.put(MealType.SNACKS, new DailyMealSummary(0.0, 0.0, 0.0, 0.0));
    }
}