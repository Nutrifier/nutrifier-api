package fi.nutrifier.controllers;

import fi.nutrifier.dto.GoalsResponse;
import fi.nutrifier.dto.GoalsUpdateRequest;
import fi.nutrifier.services.GoalsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Goals")
@RestController
@RequestMapping("/api/v1/goals")
public class GoalsController {

    private final GoalsService goalsService;

    public GoalsController(GoalsService goalsService) {
        this.goalsService = goalsService;
    }

    @Operation(summary = "Get user's goals")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<GoalsResponse> getGoals(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return goalsService.getUserGoals(userId);
    }

    @Operation(summary = "Update user goals")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping
    public ResponseEntity<GoalsResponse> updateGoals(
            Authentication authentication,
            @Valid @RequestBody GoalsUpdateRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return goalsService.update(userId, request);
    }

    @Operation(summary = "Recalculate nutrient targets")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping("/recalculate")
    public ResponseEntity<GoalsResponse> calculate(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return goalsService.recalculateGoals(userId);
    }
}
