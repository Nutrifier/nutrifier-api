package fi.nutrifier.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fi.nutrifier.dto.ProfileResponse;
import fi.nutrifier.enums.ActivityLevel;
import fi.nutrifier.enums.Sex;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "profile")
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @JsonIgnore
    @JdbcTypeCode(SqlTypes.CHAR) // Fixes "Incorrect string value: '\xD9d\xCDz)F...'" error
    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
    private UUID userId;

    private Integer height;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public ProfileResponse toResponse() {
        return new ProfileResponse(
                this.height,
                this.age,
                this.sex,
                this.activityLevel
        );
    }
}