package fi.nutrifier.unit.utils;

import fi.nutrifier.dto.UserDto;
import fi.nutrifier.entities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestObjects {

    public static String id;
    public static String userId1;
    public static String userId2;

    public static LocalDate date;

    public static UserDto user1;
    public static UserDto user2;

    public static FoodEntry foodEntry1;
    public static FoodEntry foodEntry2;
    public static FoodEntry foodEntry3;

    public static Food food1;
    public static Food food2;
    public static Food food3;

    public static UserSettings settings;

    public static UserGoals goals;

    public static MealPlan mealPlan;
    public static List<MealPlan> mealPlanList;
    public static MealPlanPeriods mealPlanPeriod;
    public static List<MealPlanPeriods> mealPlanPeriodList;


    public static void reset() {
        id = UUID.randomUUID().toString();
        userId1 = UUID.randomUUID().toString();
        userId2 = UUID.randomUUID().toString();

        date = LocalDate.of(2025, 1, 15);

        user1 = new UserDto();
        user1.setId(userId1);
        user1.initialize("test@gmail.com", Role.REGULAR);
        user2 = new UserDto();
        user2.setId(userId2);
        user2.initialize("test2@gmail.com", Role.REGULAR);

        foodEntry1 = new FoodEntry(UUID.randomUUID().toString(), date, LocalTime.of(9,0, 0), "BREAKFAST", userId1, UUID.randomUUID().toString(), null, 22.0);
        foodEntry2 = new FoodEntry(UUID.randomUUID().toString(), date, LocalTime.of(9,0, 0), "LUNCH", userId1, UUID.randomUUID().toString(), null, 120.0);
        foodEntry3 = new FoodEntry(UUID.randomUUID().toString(), date, LocalTime.of(13,0, 0), "LUNCH", userId2, UUID.randomUUID().toString(), null, 150.0);

        food1 = new Food(UUID.randomUUID().toString(), "Kanan rintafilee", "1234567890", 100, 250.0, 0.0, 0.0, 0.0, userId1, UUID.randomUUID().toString(), "", "");
        food2 = new Food(UUID.randomUUID().toString(), "Riisi (keitetty)", "1234567890", 100, 350.0, 0.0, 0.0, 0.0, userId1, UUID.randomUUID().toString(), "", "");
        food3 = new Food(UUID.randomUUID().toString(), "Kalkkunaleike", "", 100, 175.0, 0.0, 0.0, 0.0, userId2, UUID.randomUUID().toString(), "", "");

        settings = new UserSettings();
        settings.initialize();

        goals = new UserGoals();
        goals.initialize("Just for fun!", 60.0, date.plusYears(1));

        MealPlanPeriods newMealPlanPeriod = new MealPlanPeriods();
        newMealPlanPeriod.initialize(date, date.plusYears(1));
        mealPlanPeriod = newMealPlanPeriod;
        mealPlanPeriodList = new ArrayList<>();
        mealPlanPeriodList.add(mealPlanPeriod);
        MealPlan newMealPlan = new MealPlan();
        newMealPlan.initialize("My Meal Plan", mealPlanPeriodList);
        mealPlan = newMealPlan;
        mealPlanList = new ArrayList<>();
        mealPlanList.add(newMealPlan);

        List<WeightEntry> weightEntryList = new ArrayList<>();
        WeightEntry newWeightEntry = new WeightEntry();
        newWeightEntry.setDate(LocalDateTime.now());
        newWeightEntry.setWeight(84.0);
        newWeightEntry.setUser(user1.toUser());
        weightEntryList.add(newWeightEntry);
        user1.setWeightEntries(weightEntryList);
    }
}
