package fi.nutrifier.entities;

import fi.nutrifier.dto.GoalsResponse;
import fi.nutrifier.dto.GoalsUpdateRequest;
import fi.nutrifier.enums.GoalType;
import fi.nutrifier.utils.CalculationUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "goals")
@NoArgsConstructor
@AllArgsConstructor
public class Goals {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GoalType goalType;

    @Column(nullable = false)
    private LocalDate startDate;
    private LocalDate targetDate;

    @Column(nullable = false)
    private Double startWeight;
    private Double targetWeight;

    private Boolean isReached;

    @Column(name = "daily_tdee", nullable = false)
    private Double dailyTDEE;

    @Column(nullable = false) private Double dailyCalorieBalance;
    @Column(nullable = false) private Double dailyCalorieTarget;
    @Column(nullable = false) private Double dailyFatTarget;
    @Column(nullable = false) private Double dailyCarbTarget;
    @Column(nullable = false) private Double dailyProteinTarget;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GoalsResponse toResponse() {
        return new GoalsResponse(
                this.id,
                this.goalType,
                this.startDate,
                this.targetDate,
                this.startWeight,
                this.targetWeight,
                this.isReached,
                this.dailyTDEE,
                this.dailyCalorieBalance,
                this.dailyCalorieTarget,
                this.dailyFatTarget,
                this.dailyCarbTarget,
                this.dailyProteinTarget
        );
    }

    public void updateEntityFromRequest(GoalsUpdateRequest request) {
        if (request.getGoalType() != null) this.goalType = request.getGoalType();
        if (request.getTargetDate() != null) this.targetDate = request.getTargetDate();
        if (request.getTargetWeight() != null) this.targetWeight = request.getTargetWeight();
        if (request.getIsReached() != null) this.isReached = request.getIsReached();

        this.updatedAt = LocalDateTime.now();
    }

    public void calculateNutrientTargets(Profile profile, double weight) {
        // TODO: Take into account long weight loss periods which should include re-feed periods. These could be implemented with MealPlanPeriods.
        // TODO: Take into account users diet (regular, high protein, vegan...)

        double bmr = CalculationUtil.calculateMifflinStJeorBMR(profile.getSex(), weight, profile.getHeight(), profile.getAge());
        double tdee = CalculationUtil.calculateTDEE(bmr, profile.getActivityLevel());

        // Saving calculated TDEE in order to use it to calculate realized deficits
        this.setDailyTDEE(tdee);

        double dailyCalories = CalculationUtil.calculateDailyCalories(
                this.targetDate,
                this.targetWeight,
                this.goalType,
                weight,
                tdee,
                profile.getSex()
        );
        double dailyFats = CalculationUtil.calculateDailyFats(this.goalType, dailyCalories);
        double dailyProtein = CalculationUtil.calculateDailyProtein(this.goalType, weight);

        double dailyFatsCalories = CalculationUtil.dailyFatsToKcal(dailyFats);
        double dailyProteinCalories = CalculationUtil.dailyProteinToKcal(dailyProtein);

        // Calculate specific fats and protein, rest can be carbs
        double remainingCalories = dailyCalories - (dailyProteinCalories + dailyFatsCalories);
        double dailyCarbs = CalculationUtil.calculateDailyCarbs(remainingCalories);

        if (startDate == null) {
            this.startDate = LocalDate.now();
        }

        this.dailyCalorieBalance = dailyCalories - tdee;
        this.dailyCalorieTarget = dailyCalories;
        this.dailyFatTarget = dailyFats;
        this.dailyCarbTarget = dailyCarbs;
        this.dailyProteinTarget = dailyProtein;
    }
}