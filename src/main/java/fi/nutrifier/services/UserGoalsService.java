package fi.nutrifier.services;

import fi.nutrifier.dto.UserGoalsUpdateRequest;
import fi.nutrifier.entities.*;
import fi.nutrifier.repositories.GoalsRepository;
import fi.nutrifier.repositories.ProfileRepository;
import fi.nutrifier.repositories.UserRepository;
import fi.nutrifier.repositories.WeightRepository;
import fi.nutrifier.utils.CalculationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserGoalsService {

    private final GoalsRepository goalsRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final WeightRepository weightRepository;

    @Autowired
    public UserGoalsService(
            GoalsRepository goalsRepository,
            UserRepository userRepository,
            ProfileRepository profileRepository,
            WeightRepository weightRepository
    ) {
        this.goalsRepository = goalsRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.weightRepository = weightRepository;
    }

    public ResponseEntity<Goals> getUserGoals(UUID userId) {
        try {
            Goals goals = goalsRepository.findByUserId(userId).orElse(null);

            if (goals == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(goals, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Goals> update(UUID userId, UserGoalsUpdateRequest request) {
        try {
            Goals goals = goalsRepository.findByUserId(userId).orElse(null);

            if (goals == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            // Updating fields if new values given
            if (request.getGoalType() != null) {
                goals.setGoalType(request.getGoalType());
            }
            if (request.getTargetWeight() != null) {
                goals.setTargetWeight(request.getTargetWeight());
            }
            if (request.getTargetDate() != null) {
                goals.setTargetDate(request.getTargetDate());
            }
            if (request.getReachedDate() != null) {
                goals.setReachedDate(request.getReachedDate());
            }

            goals.setUpdatedAt(LocalDateTime.now());
            Goals updatedGoals = goalsRepository.save(goals);

            return new ResponseEntity<>(updatedGoals, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void createDailyNutritionSummary(Goals goals, User user) {
        List<DailyNutritionSummary> dailyNutritionSummaryList = new ArrayList<>();

        for (GoalPeriods period : goals.getPeriods()) {
            LocalDate start = period.getStartDate();
            LocalDate end = period.getEndDate();

            for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                // TODO: Check whether "existing" check is required here
                DailyNutritionSummary dailyNutritionSummary = new DailyNutritionSummary();
                dailyNutritionSummary.setUserId(user.getId());
                dailyNutritionSummary.setDate(date);
                dailyNutritionSummary.setCaloriesTarget(period.getCalorieTarget());
                dailyNutritionSummary.setFatTarget(period.getFatTarget());
                dailyNutritionSummary.setCarbTarget(period.getCarbTarget());
                dailyNutritionSummary.setProteinTarget(period.getProteinTarget());

                dailyNutritionSummary.setCaloriesConsumed(0.0);
                dailyNutritionSummary.setProteinConsumed(0.0);
                dailyNutritionSummary.setCarbsConsumed(0.0);
                dailyNutritionSummary.setFatConsumed(0.0);

                dailyNutritionSummaryList.add(dailyNutritionSummary);
            }
        }
        //dailyNutritionSummaryRepository.saveAll(dailyNutritionSummaryList);
        // TODO: Implement daily nutrition summary creation for each date
    }

    public Goals generateInitialMealPlan(User user) {
        // TODO: Take into account long weight loss periods which should include re-feed periods. These could be implemented with MealPlanPeriods.
        // TODO: Take into account users diet (regular, high protein, vegan...)

        UserProfile profile = profileRepository.findByUserId(user.getId()).orElse(null);
        Goals goals = goalsRepository.findByUserId(user.getId()).orElse(null);
        double weight = weightRepository.findByUserIdOrderByDateDesc(user.getId(), Pageable.ofSize(10)).getContent().getFirst().getWeight();

        double bmr = CalculationUtil.calculateMifflinStJeorBMR(profile.getSex(), weight, profile.getHeight(), profile.getAge());
        double tdee = CalculationUtil.calculateTDEE(bmr, profile.getActivityLevel());

        double dailyCalories = CalculationUtil.calculateDailyCalories(goals.getTargetDate(), goals.getTargetWeight(), goals.getGoalType(), weight, tdee, profile.getSex());
        double dailyFats = CalculationUtil.calculateDailyFats(goals.getGoalType(), dailyCalories);
        double dailyProtein = CalculationUtil.calculateDailyProtein(goals.getGoalType(), weight);

        double dailyFatsCalories = CalculationUtil.dailyFatsToKcal(dailyFats);
        double dailyProteinCalories = CalculationUtil.dailyProteinToKcal(dailyProtein);

        // Calculate specific fats and protein, rest can be carbs
        double remainingCalories = dailyCalories - (dailyProteinCalories + dailyFatsCalories);
        double dailyCarbs = CalculationUtil.calculateDailyCarbs(remainingCalories);

        Goals initialGoals = new Goals();
        List<GoalPeriods> goalPeriods = new ArrayList<>();
        GoalPeriods initialPeriod = new GoalPeriods();
        initialPeriod.setGoals(initialGoals);
        initialPeriod.setStartDate(LocalDate.now());
        initialPeriod.setEndDate(goals.getTargetDate());
        initialPeriod.setCalorieTarget(dailyCalories);
        initialPeriod.setFatTarget(dailyFats);
        initialPeriod.setCarbTarget(dailyCarbs);
        initialPeriod.setProteinTarget(dailyProtein);
        goalPeriods.add(initialPeriod);
        initialGoals.setPeriods(goalPeriods);

        return initialGoals;
    }

    public ResponseEntity<Goals> recalculateGoals(UUID userId) {
        try {
            User user = userRepository.findById(userId).orElse(null);

            if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            Goals goals = generateInitialMealPlan(user);
            return new ResponseEntity<>(goals, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
