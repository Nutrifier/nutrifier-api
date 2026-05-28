package fi.nutrifier.controllers;

import fi.nutrifier.dto.ApiResponse;
import fi.nutrifier.entities.WeightEntry;
import fi.nutrifier.services.UserWeightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Weights")
@RestController
@RequestMapping("/api/v1/weight")
public class WeightController {

    private final UserWeightService userWeightService;

    public WeightController(UserWeightService userWeightService) {
        this.userWeightService = userWeightService;
    }

    @Operation(summary = "Create new weigh in")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping
    public ResponseEntity<WeightEntry> createWeightEntry(Authentication authentication, @Valid @RequestBody Double weight) {
        UUID userId = UUID.fromString(authentication.getName());
        return userWeightService.create(userId, weight);
    }

    @Operation(summary = "Get weigh ins")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<Page<WeightEntry>> getWeighIns(
            Authentication authentication,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return userWeightService.getByUserId(userId, page, size);
    }
}
