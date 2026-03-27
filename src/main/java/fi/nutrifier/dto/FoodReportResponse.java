package fi.nutrifier.dto;

import fi.nutrifier.entities.FoodReport;
import fi.nutrifier.enums.ReportStatus;
import fi.nutrifier.enums.ReportType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FoodReportResponse {
    private UUID id;
    private UUID foodId;
    private UUID userId;
    private ReportType type;
    private String reason;
    private ReportStatus status;
    private String description;
    private String proposedName;
    private double proposedCalories;
    private double proposedFat;
    private double proposedCarbs;
    private double proposedProtein;
    private String decisionReasoning;
    private UUID reviewedBy;
    private LocalDateTime reviewedAt;

    public FoodReport toEntity() {
        return new FoodReport(
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
                this.reviewedAt,
                LocalDateTime.now()
        );
    }
}