package fi.nutrifier.controllers;

import fi.nutrifier.dto.UserSettingsUpdateRequest;
import fi.nutrifier.entities.Settings;
import fi.nutrifier.services.UserSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Settings")
@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final UserSettingsService userSettingsService;

    public SettingsController(UserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    @Operation(summary = "Get user settings by user id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<Settings> getSettings(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return userSettingsService.get(userId);
    }

    @Operation(summary = "Update user settings")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping
    public ResponseEntity<Settings> updateSettings(Authentication authentication, @Valid @RequestBody UserSettingsUpdateRequest item) {
        UUID userId = UUID.fromString(authentication.getName());
        return userSettingsService.update(userId, item);
    }
}
