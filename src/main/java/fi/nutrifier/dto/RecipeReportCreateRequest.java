package fi.nutrifier.dto;

import fi.nutrifier.entities.RecipeReport;
import fi.nutrifier.enums.ReportStatus;
import fi.nutrifier.enums.ReportType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RecipeReportCreateRequest {
    private ReportType type;
    private String reason;
    private ReportStatus status;
    private String description;

    public RecipeReport toEntity(UUID recipeId, UUID userId) {
        LocalDateTime now = LocalDateTime.now();
        return new RecipeReport(
                UUID.randomUUID(),
                recipeId,
                userId,
                this.getType(),
                this.getReason(),
                ReportStatus.PENDING,
                this.getDescription(),
                null,
                null,
                null,
                now
        );
    }
}