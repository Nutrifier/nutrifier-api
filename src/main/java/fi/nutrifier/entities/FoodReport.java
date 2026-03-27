package fi.nutrifier.entities;

import fi.nutrifier.dto.FoodReportCreateRequest;
import fi.nutrifier.dto.FoodReportResponse;
import fi.nutrifier.enums.ReportStatus;
import fi.nutrifier.enums.ReportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "food_reports")
@NoArgsConstructor
@AllArgsConstructor
public class FoodReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id; // Generating id in the mapper

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID foodId;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportType type;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private String description;
    private String proposedName;
    private double proposedCalories;
    private double proposedFat;
    private double proposedCarbs;
    private double proposedProtein;
    private String decisionReasoning;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID reviewedBy;

    private LocalDateTime reviewedAt;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public FoodReportResponse toResponse() {
        return new FoodReportResponse(
                this.id,
                this.foodId,
                this.userId,
                this.type,
                this.reason,
                this.status,
                this.description,
                this.proposedName,
                this.proposedCalories,
                this.proposedFat,
                this.proposedCarbs,
                this.proposedProtein,
                this.decisionReasoning,
                this.reviewedBy,
                this.reviewedAt
        );
    }

    public FoodReportCreateRequest toCreateRequest() {
        return new FoodReportCreateRequest(
                this.type,
                this.reason,
                this.description,
                this.proposedName,
                this.proposedCalories,
                this.proposedFat,
                this.proposedCarbs,
                this.proposedProtein
        );
    }
}
