package fi.nutrifier.dto;

import fi.nutrifier.entities.GoalPeriods;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class UserMealPlanUpdateRequest {
    private String name;
    private List<GoalPeriods> periods;
}
