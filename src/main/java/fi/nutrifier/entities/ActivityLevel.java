package fi.nutrifier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Data
@Table(name = "activity_levels")
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLevel {

    // Reference constants for ease of use
    public static final ActivityLevel SEDENTARY =
            new ActivityLevel(UUID.fromString("00040001-0000-0000-0000-000000000000"), "SEDENTARY", 1.2, "Mostly sitting, minimal walking");


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double multiplier;

    @Column(nullable = false)
    private String description;
}
