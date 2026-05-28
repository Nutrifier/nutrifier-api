package fi.nutrifier.dto;

import fi.nutrifier.entities.FoodEntry;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyMealSummary {
    private double caloriesConsumed;
    private double fatConsumed;
    private double carbsConsumed;
    private double proteinConsumed;

    public void appendNutritionFromEntry(FoodEntry entry) {
        this.caloriesConsumed += entry.getCaloriesSnapshot() * (entry.getAmount() / 100);
        this.fatConsumed += entry.getFatSnapshot() * (entry.getAmount() / 100);
        this.carbsConsumed += entry.getCarbsSnapshot() * (entry.getAmount() / 100);
        this.proteinConsumed += entry.getProteinSnapshot() * (entry.getAmount() / 100);
    }
}