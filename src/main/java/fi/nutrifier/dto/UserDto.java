package fi.nutrifier.dto;

import fi.nutrifier.entities.Role;
import fi.nutrifier.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
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

    public User toUser() {
        return new User(this.id, this.email, this.password, this.role);
    }
}
