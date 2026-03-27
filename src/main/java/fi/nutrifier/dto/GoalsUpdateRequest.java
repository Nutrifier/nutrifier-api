package fi.nutrifier.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fi.nutrifier.enums.GoalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class GoalsUpdateRequest {
    private GoalType goalType;
    private Double targetWeight;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reachedDate;
}
