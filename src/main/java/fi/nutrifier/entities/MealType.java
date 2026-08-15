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
@Table(name = "meal_types")
@NoArgsConstructor
@AllArgsConstructor
public class MealType {

    // TODO: Move away from the entity
    // Reference constants for ease of use
    public static final MealType BREAKFAST =
            new MealType(UUID.fromString("00020001-0000-0000-0000-000000000000"), "BREAKFAST");
    public static final MealType LUNCH =
            new MealType(UUID.fromString("00020002-0000-0000-0000-000000000000"), "LUNCH");
    public static final MealType DINNER =
            new MealType(UUID.fromString("00020003-0000-0000-0000-000000000000"), "DINNER");
    public static final MealType SNACKS =
            new MealType(UUID.fromString("00020004-0000-0000-0000-000000000000"), "SNACKS");

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;
}
