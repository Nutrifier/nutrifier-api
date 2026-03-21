package fi.nutrifier.unit.utils;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.*;
import fi.nutrifier.enums.FoodStatus;
import fi.nutrifier.enums.GoalType;
import fi.nutrifier.enums.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestObjects {

    public static UUID id;
    public static UUID userId1;
    public static UUID userId2;

    public static LocalDate date;

    public static UserResponse user1;
    public static UserResponse user2;

    public static FoodEntry foodEntry1;
    public static FoodEntry foodEntry2;
    public static FoodEntry foodEntry3;

    public static FoodEntryRequest foodEntry1Request;

    public static FoodEntryResponse foodEntry1Response;
    public static FoodEntryResponse foodEntry2Response;

    public static Food food1;
    public static Food food2;
    public static Food food3;

    public static FoodRequest foodRequest;
    public static FoodResponse foodResponse1;
    public static FoodResponse foodResponse2;

    public static Settings settings;

    public static Goals goals;

    public static GoalPeriods mealPlanPeriod;
    public static List<GoalPeriods> mealPlanPeriodList;

    public static List<WeightEntry> weightEntries;


    public static void reset() {
        id = UUID.randomUUID();
        userId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();

        date = LocalDate.of(2025, 1, 15);

        user1 = new UserResponse();
        user1.setId(userId1);
        user1.initialize("test@gmail.com", Role.REGULAR);
        user2 = new UserResponse();
        user2.setId(userId2);
        user2.initialize("test2@gmail.com", Role.REGULAR);

        foodEntry1 = new FoodEntry(UUID.randomUUID(), date, LocalTime.of(9,0, 0), "BREAKFAST", userId1, UUID.randomUUID(), null, 22.0);
        foodEntry2 = new FoodEntry(UUID.randomUUID(), date, LocalTime.of(9,0, 0), "LUNCH", userId1, UUID.randomUUID(), null, 120.0);
        foodEntry3 = new FoodEntry(UUID.randomUUID(), date, LocalTime.of(13,0, 0), "LUNCH", userId2, UUID.randomUUID(), null, 150.0);

        foodEntry1Request = new FoodEntryRequest(
                foodEntry1.getDate(),
                foodEntry1.getTime(),
                foodEntry1Request.getMealType(),
                foodEntry1Request.getFoodId(),
                foodEntry1Request.getFineliId(),
                foodEntry1Request.getAmount()
        );
        foodEntry1Response = new FoodEntryResponse(
                foodEntry1.getId(),
                foodEntry1.getDate(),
                foodEntry1.getTime(),
                foodEntry1.getMealType(),
                foodEntry1.getUserId(),
                foodEntry1.getFoodId(),
                foodEntry1.getFineliId(),
                foodEntry1.getAmount()
        );
        foodEntry2Response = new FoodEntryResponse(
                foodEntry2.getId(),
                foodEntry2.getDate(),
                foodEntry2.getTime(),
                foodEntry2.getMealType(),
                foodEntry2.getUserId(),
                foodEntry2.getFoodId(),
                foodEntry2.getFineliId(),
                foodEntry2.getAmount()
        );

        food1 = new Food(UUID.randomUUID(), "Kanan rintafilee", "brand", "category", "1234567890", 100, 250.0, 0.0, 0.0, 0.0, false, FoodStatus.ACTIVE, userId1, UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now());
        food2 = new Food(UUID.randomUUID(), "Riisi (keitetty)", "brand", "category", "1234567890", 100, 350.0, 0.0, 0.0, 0.0, false, FoodStatus.ACTIVE, userId1, UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now());
        food3 = new Food(UUID.randomUUID(), "Kalkkunaleike", "brand", "category", "", 100, 175.0, 0.0, 0.0, 0.0, false, FoodStatus.ACTIVE, userId2, UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now());

        foodRequest = new FoodRequest("Kanan rintafilee", "brand", "category", "1234567890", 100, 250.0, 0.0, 0.0, 0.0);

        foodResponse1 = new FoodResponse(UUID.randomUUID(), "Kanan rintafilee", "1234567890", 100, 250.0, 0.0, 0.0, 0.0);
        foodResponse2 = new FoodResponse(UUID.randomUUID(), "Riisi (keitetty)", "1234567890", 100, 350.0, 0.0, 0.0, 0.0);

        settings = new Settings();
        settings.initialize();

        goals = new Goals();
        //goals.initialize(GoalType.JUST_FOR_FUN, 60.0, date.plusYears(1));

        List<WeightEntry> newWeightEntries = new ArrayList<>();
        newWeightEntries.add(new WeightEntry(userId1, user1.toUser(), 70.0, LocalDateTime.now()));
        weightEntries = newWeightEntries;
    }
}
