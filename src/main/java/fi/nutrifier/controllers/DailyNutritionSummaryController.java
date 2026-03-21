package fi.nutrifier.controllers;

import fi.nutrifier.dto.DailyNutritionSummaryCreateRequest;
import fi.nutrifier.dto.DailyNutritionSummaryResponse;
import fi.nutrifier.dto.DailyNutritionSummaryUpdateRequest;
import fi.nutrifier.services.DailyNutritionSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "Daily Nutrition Summary")
@RestController
@RequestMapping("/api/user/daily-nutrition-summary")
public class DailyNutritionSummaryController {

    protected final DailyNutritionSummaryService service;

    public DailyNutritionSummaryController(DailyNutritionSummaryService service) {
        this.service = service;
    }

    @Operation(summary = "Create a daily nutrition summary")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping
    public ResponseEntity<DailyNutritionSummaryResponse> create(
            Authentication authentication,
            @Valid @RequestBody DailyNutritionSummaryCreateRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.create(userId, request);
    }

    @Operation(summary = "Update daily nutrients")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping("/{id}")
    public ResponseEntity<DailyNutritionSummaryResponse> update(
            Authentication authentication,
            @PathVariable("id") String id,
            @Valid @RequestBody DailyNutritionSummaryUpdateRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.update(userId, UUID.fromString(id), request);
    }

    @Operation(summary = "Get daily nutrients by date and user id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/by-date")
    public ResponseEntity<DailyNutritionSummaryResponse> getLogsByDateAndUser(
            Authentication authentication,
            @RequestParam String date
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        LocalDate parsedDate = LocalDate.parse(date);
        return service.getByDateAndUser(parsedDate, userId);
    }
}