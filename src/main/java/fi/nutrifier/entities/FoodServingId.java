package fi.nutrifier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodServingId implements Serializable {

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "food_id", columnDefinition = "CHAR(36)")
    private UUID foodId;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "serving_type_id", columnDefinition = "CHAR(36)")
    private UUID servingTypeId;
}
