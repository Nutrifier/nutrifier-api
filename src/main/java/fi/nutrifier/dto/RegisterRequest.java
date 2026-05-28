package fi.nutrifier.dto;

import fi.nutrifier.enums.ActivityLevel;
import fi.nutrifier.enums.GoalType;
import fi.nutrifier.enums.Sex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must have at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;

    private Sex sex;
    private Integer age;
    private Integer height;
    private ActivityLevel activityLevel;
    private GoalType goalType;
    private Double currentWeight;
    private Double targetWeight;
    private LocalDate targetDate;
}
