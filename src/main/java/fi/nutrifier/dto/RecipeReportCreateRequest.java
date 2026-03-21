package fi.nutrifier.dto;

import fi.nutrifier.enums.ReportStatus;
import fi.nutrifier.enums.ReportType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecipeReportCreateRequest {
    private ReportType type;
    private String reason;
    private ReportStatus status;
    private String description;
}