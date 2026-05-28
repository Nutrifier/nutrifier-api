package fi.nutrifier.controllers;

import fi.nutrifier.dto.DailySummaryResponse;
import fi.nutrifier.services.DailySummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "Daily Summaries")
@RestController
@RequestMapping("/api/v1/daily-summary")
public class DailySummaryController {

    protected final DailySummaryService service;

    public DailySummaryController(DailySummaryService service) {
        this.service = service;
    }

    @Operation(summary = "Calculate daily nutrients by date and user id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/by-date")
    public ResponseEntity<DailySummaryResponse> getLogsByDateAndUser(
            Authentication authentication,
            @RequestParam String date
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        LocalDate parsedDate = LocalDate.parse(date);
        return service.getAndCalculateSummary(parsedDate, userId);
    }

    @Operation(summary = "Confirm date as correctly logged")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping("/confirm")
    public ResponseEntity<DailySummaryResponse> confirmDay(
            Authentication authentication,
            @RequestParam String date
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        LocalDate parsedDate = LocalDate.parse(date);
        return service.confirmDate(parsedDate, userId);
    }
}