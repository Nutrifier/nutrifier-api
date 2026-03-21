package fi.nutrifier.controllers;

import fi.nutrifier.dto.UserProfileUpdateRequest;
import fi.nutrifier.entities.UserProfile;
import fi.nutrifier.services.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Profile")
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserProfileService service;

    public ProfileController(UserProfileService service) {
        this.service = service;
    }

    @Operation(summary = "Get user goals by user id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<UserProfile> getGoals(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.getUserGoals(userId);
    }

    @Operation(summary = "Update user goals")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping
    public ResponseEntity<UserProfile> updateGoals(Authentication authentication, @Valid @RequestBody UserProfileUpdateRequest request) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.update(userId, request);
    }
}
