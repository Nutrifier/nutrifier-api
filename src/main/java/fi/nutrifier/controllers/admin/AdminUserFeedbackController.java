package fi.nutrifier.controllers.admin;

import fi.nutrifier.dto.*;
import fi.nutrifier.services.UserFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "User feedback (Admin)")
@RestController
@RequestMapping("/api/v1/admin/user-feedback")
public class AdminUserFeedbackController {

    private final UserFeedbackService service;

    public AdminUserFeedbackController(UserFeedbackService service) {
        this.service = service;
    }

    @Operation(summary = "List all user feedbacks")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @GetMapping
    public ResponseEntity<Page<UserFeedbackResponse>> getAllFeedbacks(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.getAllFeedbacks(page, size);
    }

    @Operation(summary = "Review a user feedback")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @PatchMapping("/{id}")
    public ResponseEntity<String> reviewFeedback(
            Authentication authentication,
            @PathVariable("id") String id,
            @Valid @RequestBody UserFeedbackReviewRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.reviewFeedback(UUID.fromString(id), userId, request);
    }
}