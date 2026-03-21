package fi.nutrifier.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fi.nutrifier.enums.ActivityLevel;
import fi.nutrifier.enums.Role;
import fi.nutrifier.enums.Sex;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "user_profile")
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @JsonIgnore
    @Column(columnDefinition = "CHAR(36)", nullable = false)
    private UUID userId;

    private Integer height;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}