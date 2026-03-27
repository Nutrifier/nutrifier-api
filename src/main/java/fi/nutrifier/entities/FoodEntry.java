package fi.nutrifier.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;
import fi.nutrifier.dto.FoodEntryRequest;
import fi.nutrifier.dto.FoodEntryResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@Table(name = "food_entries")
@NoArgsConstructor
@AllArgsConstructor
public class FoodEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id; // Id formed inside mapper

    @Column(nullable = false)
    @NotNull
    @Min(value = 0)
    private Double amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    @NotNull
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(nullable = false)
    @NotNull
    private LocalTime time;

    @Column(nullable = false)
    @NotNull
    private String mealType;

    @Column(length = 5)
    private String unit;

    @Column(nullable = false)
    private Double caloriesSnapshot;

    @Column(nullable = false)
    private Double fatSnapshot;

    @Column(nullable = false)
    private Double carbsSnapshot;

    @Column(nullable = false)
    private Double proteinSnapshot;

    private Integer fineliId;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)", nullable = false)
    private UUID userId;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)", nullable = false)
    private UUID foodId;

    public FoodEntryRequest toRequest() {
        return new FoodEntryRequest(
                this.amount,
                this.date,
                this.time,
                this.mealType,
                this.unit,
                this.caloriesSnapshot,
                this.fatSnapshot,
                this.carbsSnapshot,
                this.proteinSnapshot,
                this.fineliId,
                this.foodId
        );
    }

    public FoodEntryResponse toResponse() {
        return new FoodEntryResponse(
                this.id,
                this.date,
                this.time,
                this.mealType,
                this.userId,
                this.foodId,
                this.fineliId,
                this.amount
        );
    }
}
