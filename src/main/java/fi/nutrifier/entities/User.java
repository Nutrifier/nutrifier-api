package fi.nutrifier.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fi.nutrifier.dto.SettingsUpdateRequest;
import fi.nutrifier.dto.UserResponse;
import fi.nutrifier.dto.UserUpdateRequest;
import fi.nutrifier.enums.Role;
import fi.nutrifier.exceptions.EncryptionKeyException;
import fi.nutrifier.exceptions.FailedCryptionException;
import fi.nutrifier.utils.SecurityUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public UserResponse toResponse() {
        return new UserResponse(
                this.id,
                this.email,
                this.role
        );
    }

    public void updateEntityFromRequest(UserUpdateRequest request) throws FailedCryptionException, EncryptionKeyException {
        this.email = SecurityUtil.encrypt(request.getEmail());
    }
}