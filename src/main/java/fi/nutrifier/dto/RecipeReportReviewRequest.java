package fi.nutrifier.dto;

import fi.nutrifier.enums.ReportStatus;
import fi.nutrifier.enums.ReportType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RecipeReportReviewRequest {
    private ReportType type;
    private String reason;
    private ReportStatus status;
    private String description;
    private String decisionReasoning;
    private UUID reviewedBy;
    private LocalDateTime reviewedAt;
}