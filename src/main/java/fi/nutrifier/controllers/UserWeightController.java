package fi.nutrifier.controllers;

import fi.nutrifier.dto.CreateWeighInRequest;
import fi.nutrifier.entities.WeightEntry;
import fi.nutrifier.services.UserWeightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "User Weight")
@RestController
@RequestMapping("/api/users/weight")
public class UserWeightController {

    private final UserWeightService userWeightService;

    public UserWeightController(UserWeightService userWeightService) {
        this.userWeightService = userWeightService;
    }

    @Operation(summary = "Create new weigh in")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping
    public ResponseEntity<List<WeightEntry>> updateWeight(Authentication authentication, @Valid @RequestBody CreateWeighInRequest request) {
        String userId = authentication.getName();
        return userWeightService.create(userId, request);
    }

    @Operation(summary = "Get all weigh ins")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<List<WeightEntry>> getAll(Authentication authentication) {
        String userId = authentication.getName();
        return userWeightService.getAllByUserId(userId);
    }
}
