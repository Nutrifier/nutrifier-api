package fi.nutrifier.unit.utils;

import fi.nutrifier.enums.ActivityLevel;
import fi.nutrifier.enums.FoodWeightUnit;
import fi.nutrifier.enums.GoalType;
import fi.nutrifier.enums.Sex;
import fi.nutrifier.utils.CalculationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class CalculationUtilTest {

    private static final double WEIGHT = 60.0;
    private static final int HEIGHT = 170;
    private static final int AGE = 25;
    private static final double DELTA = 0.01;

    @Nested
    @DisplayName("BMR Calculation Rules")
    class BMRTests {
        @Test
        void harrisBenedict_ShouldReflectStandardGenderGap() {
            assertEquals(1568.0, CalculationUtil.calculateHarrisBenedictBMR(Sex.MALE, WEIGHT, HEIGHT, AGE), DELTA);
            assertEquals(1419.5, CalculationUtil.calculateHarrisBenedictBMR(Sex.FEMALE, WEIGHT, HEIGHT, AGE), DELTA);
        }

        @Test
        void revisedHarrisBenedict_ShouldReflectStandardGenderGap() {
            assertEquals(1566.0870000000002, CalculationUtil.calculateRevisedHarrisBenedictBMR(Sex.MALE, WEIGHT, HEIGHT, AGE), DELTA);
            assertEquals(1420.8229999999999, CalculationUtil.calculateRevisedHarrisBenedictBMR(Sex.FEMALE, WEIGHT, HEIGHT, AGE), DELTA);
        }

        @Test
        void mifflinStJeor_ShouldReflectStandardGenderGap() {
            assertEquals(1376.5, CalculationUtil.calculateMifflinStJeorBMR(Sex.FEMALE, WEIGHT, HEIGHT, AGE));
            assertEquals(1542.5, CalculationUtil.calculateMifflinStJeorBMR(Sex.MALE, WEIGHT, HEIGHT, AGE));
        }
    }

    @Test
    @DisplayName("TDEE should scale linearly with activity level")
    void calculateTDEE_ShouldIncreaseWithActivity() {
        double bmr = 1500.0;
        double sedentaryTDEE = CalculationUtil.calculateTDEE(bmr, ActivityLevel.SEDENTARY);
        double moderateTDEE = CalculationUtil.calculateTDEE(bmr, ActivityLevel.MODERATE);
        double veryActiveTDEE = CalculationUtil.calculateTDEE(bmr, ActivityLevel.VERY_ACTIVE);

        assertTrue(veryActiveTDEE > moderateTDEE);
        assertTrue(moderateTDEE > sedentaryTDEE);
    }

    @Nested
    @DisplayName("Daily Calorie Goal Logic")
    class DailyCalorieTests {

        @ParameterizedTest(name = "Goal {0} for {1} should result in {2} kcal")
        @CsvSource({
                "MAINTAIN_WEIGHT, MALE, 2500.0",    // TDEE stays the same
                "JUST_FOR_FUN, FEMALE, 2500.0",     // Treated as maintenance
                "LOOSE_WEIGHT, MALE, 2200.0",       // Applies standard deficit (300)
                "GAIN_MUSCLE, MALE, 2800.0",        // Applies standard surplus (300)
        })
        void calculateDailyCalories_ShouldApplyReasoningCorrectly(GoalType goalType, Sex sex, double expected) {
            double tdee = 2500.0;
            LocalDate target = LocalDate.now().plusYears(1);
            double result = CalculationUtil.calculateDailyCalories(target, 70.0, goalType, 75.0, tdee, sex);
            assertEquals(expected, result, DELTA);
        }

        @ParameterizedTest(name = "Sex {0} should never drop below {1} kcal")
        @CsvSource({
                "MALE, 1500.0",
                "FEMALE, 1200.0",
                "OTHER, 1200.0"
        })
        void dailyCalories_ShouldClampToSafetyFloor(Sex sex, double floor) {
            double tdee = 1300.0;
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            double result = CalculationUtil.calculateDailyCalories(tomorrow, 40, GoalType.CUT, 100, tdee, sex);
            assertEquals(floor, result, DELTA);
        }

        @Test
        void dailyCalories_ShouldCapDeficitAt700() {
            double tdee = 3000.0;
            // Target is tomorrow, 100kg loss. Normally massive deficit, but capped at 700.
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            double result = CalculationUtil.calculateDailyCalories(tomorrow, 50, GoalType.CUT, 150, tdee, Sex.MALE);
            assertEquals(2300.0, result, DELTA);
        }
    }

    @Nested
    @DisplayName("Macro Distribution Logic")
    class MacroTests {
        @Test
        void loseWeight_ShouldHaveHigherProteinThanMaintain() {
            double weight = 70.0;
            double pLoss = CalculationUtil.calculateDailyProtein(GoalType.CUT, weight);
            double pMaintain = CalculationUtil.calculateDailyProtein(GoalType.MAINTAIN, weight);
            assertTrue(pLoss > pMaintain, "Weight loss must prioritize protein");
        }

        @Test
        void macrosShouldSumToCalorieGoal() {
            assertMacrosAreValid(GoalType.CUT, 56.0, 220.0, 154.0);
            assertMacrosAreValid(GoalType.BULK, 56.0, 234.0, 140.0);
            assertMacrosAreValid(GoalType.MAINTAIN, 60.0, 260.0, 105.0);
        }

        private void assertMacrosAreValid(GoalType goal, double expectedFats, double expectedCarbs, double expectedProtein) {
            double calorieGoal = 2000;
            double dailyFats = CalculationUtil.calculateDailyFats(goal, calorieGoal);
            double dailyProtein = CalculationUtil.calculateDailyProtein(goal, 70.0);

            double fatCalories = dailyFats * 9;
            double proteinCalories = dailyProtein * 4;
            double dailyCarbs = CalculationUtil.calculateDailyCarbs(calorieGoal - (fatCalories + proteinCalories));

            double totalCalories = fatCalories + proteinCalories + (dailyCarbs * 4);
            assertEquals(calorieGoal, totalCalories, 0.01, "Total calories must match the goal");

            assertEquals(expectedFats, dailyFats, 0.01, "Fats calculation mismatch");
            assertEquals(expectedProtein, dailyProtein, 0.01, "Protein calculation mismatch");
            assertEquals(expectedCarbs, dailyCarbs, 0.01, "Carbs calculation mismatch");
        }
    }

    @Nested
    @DisplayName("Amount Normalization (grams only)")
    class NormalizationTests {

        private static final double DELTA = 1e-6;

        @ParameterizedTest(name = "{1}g with {0}/100g → {2}g")
        @CsvSource({
                "100.0, 100.0, 100.0",
                "100.0, 50.0, 50.0",
                "50.0, 100.0, 50.0",
                "50.0, 50.0, 25.0",
                "100.0, 0.0, 0.0",
                "0.0, 100.0, 0.0",
                "100.0, 0.001, 0.001",
                "100.0, 10000.0, 10000.0"
        })
        void shouldNormalizeCorrectly(double nutritionPer100g, double grams, double expected) {
            double result = CalculationUtil.calculateAmountFromRequest(
                    nutritionPer100g,
                    grams,
                    FoodWeightUnit.GRAMS
            );

            assertEquals(expected, result, DELTA);
        }
    }

    @Nested
    @DisplayName("Amount Calculation Integration")
    class IntegrationTests {

        private static final double DELTA = 1e-4;

        @ParameterizedTest(name = "{1} {2} with {0}/100g → {3}g")
        @CsvSource({
                "100.0, 100.0, GRAMS, 100.0",
                "100.0, 0.2204623, POUNDS, 100.0",
                "100.0, 3.5273962, OUNCES, 100.0",
                "50.0, 0.11023115, POUNDS, 25.0",
                "50.0, 1.7636981, OUNCES, 25.0"
        })
        void shouldCalculateAmountCorrectly(double nutritionPer100g, double amount, FoodWeightUnit unit, double expected) {
            double result = CalculationUtil.calculateAmountFromRequest(
                    nutritionPer100g,
                    amount,
                    unit
            );

            assertEquals(expected, result, DELTA);
        }
    }
}