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
@IdClass(UserIdFoodId.class)
@Table(name = "favourite_foods")
@NoArgsConstructor
@AllArgsConstructor
public class FoodFavourite {

    @Id
    @Column(columnDefinition = "CHAR(36)")
    private UUID userId;

    @Id
    @Column(columnDefinition = "CHAR(36)")
    private UUID foodId;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
