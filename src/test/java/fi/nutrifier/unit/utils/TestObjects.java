package fi.nutrifier.unit.utils;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.*;
import fi.nutrifier.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestObjects {

    public static UUID id = UUID.randomUUID();

    public static UUID id1 = UUID.nameUUIDFromBytes("testId1".getBytes());
    public static UUID id2 = UUID.nameUUIDFromBytes("testId2".getBytes());
    public static UUID id3 = UUID.nameUUIDFromBytes("testId3".getBytes());

    public static LocalDate date;

    public static RegisterRequest registerRequest;

    public static UserResponse user1;
    public static UserResponse user2;

    public static FoodEntry foodEntry1;
    public static FoodEntry foodEntry2;
    public static FoodEntry foodEntry3;

    public static Food food1;
    public static Food food2;
    public static Food food3;

    public static FoodReport foodReport1;
    public static FoodReport foodReport2;

    public static AuditLog auditLog1;
    public static AuditLog auditLog2;

    public static Meal meal;

    public static Recipe recipe;

    public static RecipeReport recipeReport;

    public static UserFeedback userFeedback;

    public static DailySummaryResponse dailySummary;

    public static Settings settings;

    public static Goals goals;

    public static List<WeightEntry> weightEntries;

    public static Profile profile;


    public static void reset() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        registerRequest = new RegisterRequest(
                "test@gmail.com",
                "Qwerty123!",
                Sex.FEMALE,
                18,
                180,
                ActivityLevel.SEDENTARY,
                GoalType.BULK,
                80.0,
                85.0,
                LocalDate.of(now.plusYears(1).getYear(), 1, 1)
        );

        date = LocalDate.of(2025, 1, 15);

        user1 = new UserResponse(id1, "test@gmail.com", Role.REGULAR);
        user2 = new UserResponse(id2, "test2@gmail.com", Role.REGULAR);

        food1 = new Food(id1, "Kanan rintafilee", "brand", "category", "1234567890", 100, 250.0, 0.0, 0.0, 0.0, false, FoodStatus.ACTIVE, id1, id1, now, now);
        food2 = new Food(id2, "Riisi (keitetty)", "brand", "category", "1234567890", 100, 350.0, 0.0, 0.0, 0.0, false, FoodStatus.ACTIVE, id2, id2, now, now);
        food3 = new Food(id3, "Kalkkunaleike", "brand", "category", "", 100, 175.0, 0.0, 0.0, 0.0, false, FoodStatus.ACTIVE, id3, id3, now, now);

        foodReport1 = new FoodReport(id1, id1, id1, ReportType.UPDATE_REQUEST, "Incorrect values", ReportStatus.APPROVED, "description", "proposedName", 0.0, 0.0, 0.0, 0.0, "decision reasoning", id2, now, now);
        foodReport2 = new FoodReport(id2, id2, id2, ReportType.REPORT, "Bad name", ReportStatus.PENDING, "description", "proposedName", 0.0, 0.0, 0.0, 0.0, null, null, null, now);

        foodEntry1 = new FoodEntry(id1, 22.0, date, LocalTime.of(9,0, 0), MealType.BREAKFAST, FoodWeightUnit.GRAMS, 120.0, 12.0, 50.0, 24.0, null, id1, id1);
        foodEntry2 = new FoodEntry(id2, 120.0, date, LocalTime.of(9,0, 0), MealType.LUNCH, FoodWeightUnit.GRAMS, 120.0, 12.0, 50.0, 24.0, null, id1, id2);
        foodEntry3 = new FoodEntry(id3, 150.0, date, LocalTime.of(13,0, 0), MealType.LUNCH, FoodWeightUnit.GRAMS, 120.0, 12.0, 50.0, 24.0, null, id2, id3);

        auditLog1 = new AuditLog(id1, id1, "Logged in", "AUTH", "source", now, null, null, null, now);
        auditLog2 = new AuditLog(id1, id1, "Created a food", "FOOD", "source", now, null, null, null, now);

        List<MealEntry> entries = new ArrayList<>();
        entries.add(new MealEntry(id1, null, food1, 100.0, "g"));
        meal = new Meal(id1, id1, "My meal", false, false, now, now, entries);

        List<RecipeStep> steps = new ArrayList<>();
        steps.add(new RecipeStep(id1, null, 1, "Preheat the oven", now, now));
        List<RecipeIngredientSection> ingredientSections = new ArrayList<>();
        List<RecipeIngredient> ingredients = new ArrayList<>();
        ingredients.add(new RecipeIngredient(id1, null, food1, 100.0, "g", now, now));
        ingredientSections.add(new RecipeIngredientSection(id, null, 1, now, now, ingredients));
        recipe = new Recipe(id1, id1, "My recipe", "description", 8, 40, false, false, now, now, steps, ingredientSections);

        recipeReport = new RecipeReport(id1, id1, id1, ReportType.UPDATE_REQUEST, "Wrong values", ReportStatus.PENDING, "description", null, null, null, now);

        userFeedback = new UserFeedback(id1, id1, FeedbackType.BUG, "My feedback", "message", FeedbackStatus.PENDING, null, null, null, now);

        dailySummary = new DailySummaryResponse(120.0, 50.0, 200.0, 150.0);

        settings = new Settings();
        settings.initialize();

        goals = new Goals(id1, id1, GoalType.MAINTAIN, today, today.plusYears(1), 60.0, 80.0, false, 2400.0, -300.0, 1500.0, 40.0, 70.0, 50.0, now, now);

        List<WeightEntry> newWeightEntries = new ArrayList<>();
        newWeightEntries.add(new WeightEntry(id1, id1, 70.0, LocalDateTime.now()));
        weightEntries = newWeightEntries;

        profile = new Profile(id1, 180, 18, Sex.FEMALE, ActivityLevel.SEDENTARY, now);
    }
}
