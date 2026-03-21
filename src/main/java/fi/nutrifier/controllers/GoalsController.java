package fi.nutrifier.controllers;

import fi.nutrifier.dto.UserGoalsUpdateRequest;
import fi.nutrifier.entities.Goals;
import fi.nutrifier.services.UserGoalsService;
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
@RequestMapping("/api/goals")
public class GoalsController {

    private final UserGoalsService userGoalsService;

    public GoalsController(UserGoalsService userGoalsService) {
        this.userGoalsService = userGoalsService;
    }

    @Operation(summary = "Get user goals by user id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<Goals> getGoals(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return userGoalsService.getUserGoals(userId);
    }

    @Operation(summary = "Update user goals")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping
    public ResponseEntity<Goals> updateGoals(
            Authentication authentication,
            @Valid @RequestBody UserGoalsUpdateRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return userGoalsService.update(userId, request);
    }

    @Operation(summary = "Calculate new meal plan")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/calculate")
    public ResponseEntity<Goals> calculate(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return userGoalsService.recalculateGoals(userId);
    }
}
