package fi.nutrifier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "favourite_meals")
@IdClass(UserIdMealId.class)
@NoArgsConstructor
@AllArgsConstructor
public class MealFavourite {

    @Id
    @Column(columnDefinition = "CHAR(36)")
    private UUID userId;

    @Id
    @Column(columnDefinition = "CHAR(36)")
    private UUID mealId;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
