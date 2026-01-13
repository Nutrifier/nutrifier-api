package fi.nutrifier.controllers;

import fi.nutrifier.dto.UserGoalsUpdateRequest;
import fi.nutrifier.dto.UserSettingsUpdateRequest;
import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.entities.UserGoals;
import fi.nutrifier.services.UserGoalsService;
import fi.nutrifier.services.UserSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "User Goals")
@RestController
@RequestMapping("/api/users/goals")
public class UserGoalsController {

    private final UserGoalsService userGoalsService;

    public UserGoalsController(UserGoalsService userGoalsService) {
        this.userGoalsService = userGoalsService;
    }

    @Operation(summary = "Get user goals by user id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<UserGoals> getGoals(Authentication authentication) {
        String userId = authentication.getName();
        return userGoalsService.getUserGoals(userId);
    }

    @Operation(summary = "Update user goals")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping
    public ResponseEntity<UserGoals> updateGoals(Authentication authentication, @Valid @RequestBody UserGoalsUpdateRequest request) {
        String userId = authentication.getName();
        return userGoalsService.update(userId, request);
    }
}
