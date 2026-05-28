package fi.nutrifier.utils;

import fi.nutrifier.enums.ActivityLevel;
import fi.nutrifier.enums.FoodWeightUnit;
import fi.nutrifier.enums.GoalType;
import fi.nutrifier.enums.Sex;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculationUtil {

    private final static double SEDENTARY_MULTIPLIER = 1.2;
    private final static double LIGHT_MULTIPLIER = 1.375;
    private final static double MODERATE_MULTIPLIER = 1.55;
    private final static double ACTIVE_MULTIPLIER = 1.725;
    private final static double VERY_ACTIVE_MULTIPLIER = 1.9;
    private final static double GRAM_TO_LB_MULTIPLIER = 453.59237;
    private final static double GRAM_TO_OZ_MULTIPLIER = 28.349523125;

    private final static double FAT_TO_KCAL_MULTIPLIER = 9;
    private final static double CARB_PROTEIN_KCAL_MULTIPLIER = 4;

    public final static double CALORIE_FAIL_OFFSET = 80.0;

    private static double calculateBMR(double base, double weightMultiplier, double heightMultiplier, double ageMultiplier, double weight, int height, int age) {
        return base + (weightMultiplier * weight) + (heightMultiplier * height) - (ageMultiplier * age);
    }

    public static double calculateHarrisBenedictBMR(Sex sex, double weight, int height, int age) {
        double base = 655.0;
        double weightMultiplier = 9.6;
        double heightMultiplier = 1.8;
        double ageMultiplier = 4.7;

        if (sex == Sex.MALE) {
            base = 66.0;
            weightMultiplier = 13.7;
            heightMultiplier = 5;
            ageMultiplier = 6.8;
        }

        return calculateBMR(base, weightMultiplier, heightMultiplier, ageMultiplier, weight, height, age);
    }

    // Source: https://www.garnethealth.org/news/basal-metabolic-rate-calculator
    public static double calculateRevisedHarrisBenedictBMR(Sex sex, double weight, int height, int age) {
        double base = 447.593;
        double weightMultiplier = 9.247;
        double heightMultiplier = 3.098;
        double ageMultiplier = 4.330;

        if (sex == Sex.MALE) {
            base = 88.362;
            weightMultiplier = 13.397;
            heightMultiplier = 4.799;
            ageMultiplier = 5.677;
        }

        return calculateBMR(base, weightMultiplier, heightMultiplier, ageMultiplier, weight, height, age);
    }

    public static double calculateMifflinStJeorBMR(Sex sex, double weight, int height, int age) {
        double base = -161;
        double weightMultiplier = 10;
        double heightMultiplier = 6.25;
        double ageMultiplier = 5;

        if (sex == Sex.MALE) {
            base = 5;
        }

        return calculateBMR(base, weightMultiplier, heightMultiplier, ageMultiplier, weight, height, age);
    }

    // Total daily energy expenditure
    public static int calculateTDEE(double bmr, ActivityLevel activityLevel) {
        switch (activityLevel) {
            case LIGHT -> {
                return (int) Math.round(bmr * LIGHT_MULTIPLIER);
            }
            case MODERATE -> {
                return (int) Math.round(bmr * MODERATE_MULTIPLIER);
            }
            case ACTIVE -> {
                return (int) Math.round(bmr * ACTIVE_MULTIPLIER);
            }
            case VERY_ACTIVE -> {
                return (int) Math.round(bmr * VERY_ACTIVE_MULTIPLIER);
            }
            default -> {
                return (int) Math.round(bmr * SEDENTARY_MULTIPLIER);
            }
        }
    }

    // https://www.health.harvard.edu/staying-healthy/calorie-counting-made-easy
    public static int minCaloriesForSex(Sex sex) {
        if (sex == Sex.MALE) {
            return 1500;
        }
        return 1200;
    }

    public static double calculateDailyCalories(LocalDate targetDate, double targetWeight, GoalType goalType, double weight, double tdee, Sex sex) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), targetDate);
        if (days <= 0) throw new IllegalArgumentException("Target date must be in the future");

        double calorieGoal;
        double deltaKg = targetWeight - weight;

        switch (goalType) {
            case CUT -> {
                // 1 kg of fat is approximately 7700 kcal
                double dailyDelta = (deltaKg * 7700) / days;
                // TODO: Notify the user that the goal is ambitious
                dailyDelta = Math.clamp(dailyDelta, -700, -300); // Restricting too big of a caloric deficit
                calorieGoal = tdee + dailyDelta;
            }
            case BULK -> {
                // Safe surplus range is 300-500 for muscle gain
                // https://blog.nasm.org/how-to-clean-bulk
                calorieGoal = tdee + 300;
            }
            default -> calorieGoal = tdee;
        }

        return Math.max(calorieGoal, minCaloriesForSex(sex));
    }

    public static double calculateDailyFats(GoalType goalType, double calorieGoal) {
        double fatPercent;
        if (goalType == GoalType.MAINTAIN) {
            fatPercent = 0.27;
        } else {
            fatPercent = 0.25;
        }
        double fatCalories = calorieGoal * fatPercent;
        return Math.round(fatCalories / 9); // 1 g fat = 9 kcal
    }

    public static double calculateDailyCarbs(double remainingCalories) {
        return Math.round(remainingCalories / 4); // 1 g carbs = 4 kcal
    }

    public static double calculateDailyProtein(GoalType goalType, double weight) {
        double proteinPerKg;
        switch (goalType) {
            case CUT -> proteinPerKg = 2.2; // TODO: Range from 1.6 to 2.4 grams
            case BULK -> proteinPerKg = 2.0; // TODO: Range from 1.6 to 2.2 grams
            default -> proteinPerKg = 0.8;
        }
        return Math.round(proteinPerKg * weight);
    }

    public static double dailyProteinToKcal(double dailyProtein) {
        return dailyProtein * 4;
    }

    public static double dailyFatsToKcal(double dailyFats) {
        return dailyFats * 9;
    }

    public static double calculateAmountFromRequest(double nutritionValuePer100g, double amount, FoodWeightUnit unit) {
        switch (unit) {
            case POUNDS -> {
                return nutritionValuePer100g * (amount * GRAM_TO_LB_MULTIPLIER) / 100.0;
            }
            case OUNCES -> {
                return nutritionValuePer100g * (amount * GRAM_TO_OZ_MULTIPLIER) / 100.0;
            }
            default -> {
                return nutritionValuePer100g * amount / 100.0;
            }
        }
    }

    public static double calculateCaloriesFromMacros(double fat, double carbs, double protein) {
        return (fat * FAT_TO_KCAL_MULTIPLIER) + (carbs * CARB_PROTEIN_KCAL_MULTIPLIER) + (protein * CARB_PROTEIN_KCAL_MULTIPLIER);
    }
}
