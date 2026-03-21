package fi.nutrifier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @Column(columnDefinition = "CHAR(36)")
    private UUID userId;

    @Id
    @Column(columnDefinition = "CHAR(36)")
    private UUID foodId;

    @Column(nullable = false)
    private int usageCount;

    @Column(nullable = false)
    private LocalDateTime lastUsedAt;
}
