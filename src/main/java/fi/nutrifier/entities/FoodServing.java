package fi.nutrifier.entities;

import fi.nutrifier.enums.ServingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Data
@Table(name = "food_servings")
@NoArgsConstructor
@AllArgsConstructor
public class FoodServing {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID foodId;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private ServingType type;

    @Column(nullable = false)
    @NotNull
    @Min(value = 0)
    private Double amount;
}
