package fi.nutrifier.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
public class DailySummaryMealId implements Serializable {

    private UUID dailySummaryId;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "meal_type_id", columnDefinition = "CHAR(36)")
    private UUID mealTypeId;
}
