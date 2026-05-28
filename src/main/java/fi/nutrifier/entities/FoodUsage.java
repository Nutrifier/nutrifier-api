package fi.nutrifier.entities;

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
@Table(name = "food_usage")
@IdClass(UserIdFoodId.class)
@NoArgsConstructor
@AllArgsConstructor
public class FoodUsage {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID userId;

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID foodId;

    @Column(nullable = false)
    private int usageCount;

    @Column(nullable = false)
    private LocalDateTime lastUsedAt;
}
