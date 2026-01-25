package fi.nutrifier.controllers;

import fi.nutrifier.dto.UserSettingsUpdateRequest;
import fi.nutrifier.entities.UserGoals;
import fi.nutrifier.entities.UserSettings;
import fi.nutrifier.services.UserSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Settings")
@RestController
@RequestMapping("/api/users/settings")
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    public UserSettingsController(UserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    @Operation(summary = "Get user settings by user id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<UserSettings> getSettings(Authentication authentication) {
        String userId = authentication.getName();
        return userSettingsService.get(userId);
    }

    @Operation(summary = "Update user settings")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping
    public ResponseEntity<UserSettings> updateSettings(Authentication authentication, @Valid @RequestBody UserSettingsUpdateRequest item) {
        String userId = authentication.getName();
        return userSettingsService.update(userId, item);
    }
}
