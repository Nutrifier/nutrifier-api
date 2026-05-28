package fi.nutrifier.entities;

import fi.nutrifier.dto.*;
import fi.nutrifier.enums.FoodStatus;
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

    public FoodReport reportCreateRequestToEntity(UUID foodId, UUID userId, FoodReportCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return new FoodReport(
                UUID.randomUUID(),
                foodId,
                userId,
                request.getType(),
                request.getReason(),
                ReportStatus.PENDING,
                request.getDescription(),
                request.getProposedName(),
                request.getProposedCalories(),
                request.getProposedFat(),
                request.getProposedCarbs(),
                request.getProposedProtein(),
                null,
                null,
                null,
                now
        );
    }

    public void reportUpdateRequestToEntity(UUID userId, FoodReportReviewRequest request) {
        LocalDateTime now = LocalDateTime.now();
        this.setDecisionReasoning(request.getDecisionReasoning());
        this.setReviewedBy(userId);
        this.setReviewedAt(now);
    }

    public FoodReportResponse reportEntityToResponse(FoodReport report) {
        return new FoodReportResponse(
                report.getId(),
                report.getFoodId(),
                report.getUserId(),
                report.getType(),
                report.getReason(),
                report.getStatus(),
                report.getDescription(),
                report.getProposedName(),
                report.getProposedCalories(),
                report.getProposedFat(),
                report.getProposedCarbs(),
                report.getProposedProtein(),
                report.getDecisionReasoning(),
                report.getReviewedBy(),
                report.getReviewedAt()
        );
    }
}
