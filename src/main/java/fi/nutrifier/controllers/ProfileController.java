package fi.nutrifier.controllers;

import fi.nutrifier.dto.ProfileResponse;
import fi.nutrifier.dto.ProfileUpdateRequest;
import fi.nutrifier.services.ProfileService;
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

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @Operation(summary = "Get user profile")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.getProfile(userId);
    }

    @Operation(summary = "Update user profile")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping
    public ResponseEntity<ProfileResponse> updateGoals(Authentication authentication, @Valid @RequestBody ProfileUpdateRequest request) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.update(userId, request);
    }
}
