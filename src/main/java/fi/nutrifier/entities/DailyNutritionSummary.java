package fi.nutrifier.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import fi.nutrifier.dto.DailyNutritionSummaryCreateRequest;
import fi.nutrifier.dto.DailyNutritionSummaryResponse;
import fi.nutrifier.dto.DailyNutritionSummaryUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "daily-nutrition_summary")
@NoArgsConstructor
@AllArgsConstructor
public class DailyNutritionSummary {

    @Id
    @Column(columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)", nullable = false)
    private UUID userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private double caloriesTarget;

    @Column(nullable = false)
    private double fatTarget;

    @Column(nullable = false)
    private double carbTarget;

    @Column(nullable = false)
    private double proteinTarget;

    @Column(nullable = false)
    private double caloriesConsumed;

    @Column(nullable = false)
    private double fatConsumed;

    @Column(nullable = false)
    private double carbsConsumed;

    @Column(nullable = false)
    private double proteinConsumed;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public DailyNutritionSummaryResponse toResponse() {
        return new DailyNutritionSummaryResponse(
                this.id,
                this.date,
                this.caloriesTarget,
                this.fatTarget,
                this.carbTarget,
                this.proteinTarget,
                this.caloriesConsumed,
                this.fatConsumed,
                this.carbsConsumed,
                this.proteinConsumed
        );
    }

    public DailyNutritionSummaryCreateRequest toCreateRequest() {
        return new DailyNutritionSummaryCreateRequest(
                this.date,
                this.caloriesTarget,
                this.fatTarget,
                this.carbTarget,
                this.proteinTarget
        );
    }

    public DailyNutritionSummaryUpdateRequest toUpdateRequest() {
        return new DailyNutritionSummaryUpdateRequest(
                this.date,
                this.caloriesTarget,
                this.fatTarget,
                this.carbTarget,
                this.proteinTarget,
                this.caloriesConsumed,
                this.fatConsumed,
                this.carbsConsumed,
                this.proteinConsumed
        );
    }
}
