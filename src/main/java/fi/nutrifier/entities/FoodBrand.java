package fi.nutrifier.entities;

import fi.nutrifier.utils.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Data
@Table(name = "food_brands")
@NoArgsConstructor
@AllArgsConstructor
public class FoodBrand {

    // Reference constants for ease of use
    public static final FoodBrand FINELI =
            new FoodBrand(Constants.FINELI_UUID, "Fineli", true);

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Boolean verified;
}
