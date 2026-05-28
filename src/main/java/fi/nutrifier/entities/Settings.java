package fi.nutrifier.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fi.nutrifier.dto.GoalsUpdateRequest;
import fi.nutrifier.dto.SettingsUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "settings")
@NoArgsConstructor
@AllArgsConstructor
public class Settings {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    private UUID userId;

    @Column(name = "weight_unit", nullable = false, length = 10)
    private String weightUnit;

    @Column(name = "energy_unit", nullable = false, length = 5)
    private String energyUnit;

    @Column(name = "nutrient_display_mode", nullable = false, length = 20)
    private String nutrientDisplayMode;

    @Column(name = "language", nullable = false, length = 5)
    private String language;

    @Column(name = "time_between_meals")
    private Integer timeBetweenMeals;

    @Column(name = "diet", length = 20)
    private String diet;

    @Column(name = "week_starts_on", nullable = false)
    private Integer weekStartsOn;

    @Column(name = "protein_efficiency_enabled", nullable = false)
    private Boolean proteinEfficiencyEnabled;

    @Column(name = "meal_reminder_enabled", nullable = false)
    private Boolean mealReminderEnabled;

    @Column(name = "weigh_in_reminder_enabled", nullable = false)
    private Boolean weighInReminderEnabled;

    @Column(name = "motivation_messages_enabled", nullable = false)
    private Boolean motivationMessagesEnabled;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void initialize() {
        this.setWeightUnit("G");
        this.setEnergyUnit("KCAL");
        this.setNutrientDisplayMode("FULL_CIRCLE");
        this.setLanguage("EN");
        this.setTimeBetweenMeals(3);
        this.setDiet("STANDARD");
        this.setWeekStartsOn(1);
        this.setProteinEfficiencyEnabled(true);
        this.setMealReminderEnabled(true);
        this.setWeighInReminderEnabled(true);
        this.setMotivationMessagesEnabled(true);
        this.setUpdatedAt(LocalDateTime.now());
    }

    public void updateEntityFromRequest(SettingsUpdateRequest request) {
        if (request.getWeightUnit() != null) {
            this.weightUnit = request.getWeightUnit();
        }
        if (request.getEnergyUnit() != null) {
            this.energyUnit = request.getEnergyUnit();
        }
        if (request.getNutrientDisplayMode() != null) {
            this.nutrientDisplayMode = request.getNutrientDisplayMode();
        }
        if (request.getLanguage() != null) {
            this.language = request.getLanguage();
        }
        if (request.getTimeBetweenMeals() != null) {
            this.timeBetweenMeals = request.getTimeBetweenMeals();
        }
        if (request.getDiet() != null) {
            this.diet = request.getDiet();
        }
        if (request.getWeekStartsOn() != null) {
            this.weekStartsOn = request.getWeekStartsOn();
        }
        if (request.getProteinEfficiencyEnabled() != null) {
            this.proteinEfficiencyEnabled = request.getProteinEfficiencyEnabled();
        }
        if (request.getMealReminderEnabled() != null) {
            this.mealReminderEnabled = request.getMealReminderEnabled();
        }
        if (request.getWeighInReminderEnabled() != null) {
            this.weighInReminderEnabled = request.getWeighInReminderEnabled();
        }
        if (request.getMotivationMessagesEnabled() != null) {
            this.motivationMessagesEnabled = request.getMotivationMessagesEnabled();
        }
        this.updatedAt = LocalDateTime.now();
    }
}