package fi.nutrifier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SettingsUpdateRequest {
    private String weightUnit;
    private String macroWeightUnit;
    private String energyUnit;
    private String nutrientDisplayMode;
    private String language;
    private Integer timeBetweenMeals;
    private String diet;
    private Integer weekStartsOn;
    private Boolean proteinEfficiencyEnabled;
    private Boolean mealReminderEnabled;
    private Boolean weighInReminderEnabled;
    private Boolean motivationMessagesEnabled;
    private LocalDateTime updatedAt;
}
