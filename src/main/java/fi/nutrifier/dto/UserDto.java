package fi.nutrifier.dto;

import fi.nutrifier.entities.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String id;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must have at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;

    private Role role;

    private UserSettings settings;
    private UserGoals goals;
    private List<MealPlan> mealPlans;
    private List<WeightEntries> weightEntries = new ArrayList<>();

    public void initialize(String email, String password, Role role) {
        this.setEmail(email);
        this.setPassword(password);
        this.setRole(role);
    }

    public User toUser() {
        return new User(this.id, this.email, this.password, this.role, this.settings, this.goals, this.mealPlans, this.weightEntries);
    }
}
