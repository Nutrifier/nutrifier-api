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
public class FoodReportCreateRequest {
    private ReportType type;
    private String reason;
    private String description;
    private String proposedName;
    private double proposedCalories;
    private double proposedFat;
    private double proposedCarbs;
    private double proposedProtein;

    public FoodReport toEntity(UUID foodId, UUID userId) {
        LocalDateTime now = LocalDateTime.now();
        return new FoodReport(
                UUID.randomUUID(),
                foodId,
                userId,
                this.type,
                this.reason,
                ReportStatus.PENDING,
                this.description,
                this.proposedName,
                this.proposedCalories,
                this.proposedFat,
                this.proposedCarbs,
                this.proposedProtein,
                null,
                null,
                null,
                now
        );
    }
}