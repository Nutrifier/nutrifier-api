package fi.nutrifier.dto;

import fi.nutrifier.enums.ReportType;
import lombok.AllArgsConstructor;
import lombok.Data;

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
}