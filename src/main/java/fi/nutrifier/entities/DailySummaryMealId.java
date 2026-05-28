package fi.nutrifier.entities;

import fi.nutrifier.enums.MealType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
public class DailySummaryMealId implements Serializable {

    private UUID dailySummaryId;

    @Enumerated(EnumType.STRING)
    private MealType mealType;
}
