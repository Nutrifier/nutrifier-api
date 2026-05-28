package fi.nutrifier.controllers;

import fi.nutrifier.dto.AnalyticsFullResponse;
import fi.nutrifier.enums.AnalyticsTimePeriod;
import fi.nutrifier.services.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "Analytics")
@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    protected final AnalyticsService service;

    public AnalyticsController(AnalyticsService service) {
        this.service = service;
    }

    @Operation(summary = "Get user's analytics")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping()
    public ResponseEntity<AnalyticsFullResponse> getAnalytics(
            Authentication authentication,
            @RequestParam String date,
            @RequestParam(required = false) String period
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        LocalDate parsedDate = LocalDate.parse(date);
        AnalyticsTimePeriod parsedPeriod = period != null ? AnalyticsTimePeriod.valueOf(period) : AnalyticsTimePeriod.WEEK;
        return service.calculateAnalyticsWithinDateRange(parsedDate, userId, parsedPeriod);
    }
}