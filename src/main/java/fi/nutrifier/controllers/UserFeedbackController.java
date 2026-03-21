package fi.nutrifier.controllers;

import fi.nutrifier.dto.UserFeedbackCreateRequest;
import fi.nutrifier.services.UserFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "User feedback")
@RestController
@RequestMapping("/api/user-feedback")
public class UserFeedbackController {

    protected final UserFeedbackService service;

    public UserFeedbackController(UserFeedbackService service) {
        this.service = service;
    }

    @Operation(summary = "Create a user feedback")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping
    public ResponseEntity<String> createFeedback(
            Authentication authentication,
            @Valid @RequestBody UserFeedbackCreateRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.createFeedback(userId, request);
    }
}