package fi.nutrifier.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "meal_plan_periods")
@NoArgsConstructor
@AllArgsConstructor
public class MealPlanPeriods {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_plan_id", nullable = false)
    @JsonIgnore // No need to print out meal plan here
    private MealPlan mealPlan;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "daily_calories", nullable = false)
    private Double dailyCalories;

    @Column(name = "daily_carbs", nullable = false)
    private Double dailyCarbs;

    @Column(name = "daily_fat", nullable = false)
    private Double dailyFat;

    @Column(name = "daily_protein", nullable = false)
    private Double dailyProtein;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void initialize(LocalDate startDate, LocalDate endDate) {
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }
}