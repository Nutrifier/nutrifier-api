package fi.nutrifier.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Data
@Table(name = "meal_entries")
@NoArgsConstructor
@AllArgsConstructor
public class MealEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id; // Generating id in the mapper

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", columnDefinition = "CHAR(36)", nullable = false)
    @JsonIgnore // No need to print out meal plan here
    @ToString.Exclude
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", columnDefinition = "CHAR(36)", nullable = false)
    @JsonIgnore // No need to print out meal plan here
    @ToString.Exclude
    private Food food;

    @Column(nullable = false)
    private double amount;

    @Column(length = 5)
    private String unit;
}
