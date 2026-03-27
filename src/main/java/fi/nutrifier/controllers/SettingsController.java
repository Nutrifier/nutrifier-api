package fi.nutrifier.controllers;

import fi.nutrifier.dto.SettingsUpdateRequest;
import fi.nutrifier.entities.Settings;
import fi.nutrifier.services.SettingsService;
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

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Operation(summary = "Get user settings by user id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<Settings> getSettings(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return settingsService.get(userId);
    }

    @Operation(summary = "Update user settings")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping
    public ResponseEntity<Settings> updateSettings(Authentication authentication, @Valid @RequestBody SettingsUpdateRequest item) {
        UUID userId = UUID.fromString(authentication.getName());
        return settingsService.update(userId, item);
    }
}
