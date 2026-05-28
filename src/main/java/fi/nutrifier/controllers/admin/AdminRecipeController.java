package fi.nutrifier.controllers.admin;

import fi.nutrifier.dto.*;
import fi.nutrifier.services.MealService;
import fi.nutrifier.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Recipes (Admin)")
@RestController
@RequestMapping("/api/v1/admin/recipes")
public class AdminRecipeController {

    private final RecipeService service;

    public AdminRecipeController(RecipeService service) {
        this.service = service;
    }

    @Operation(summary = "Get all recipes")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<Page<RecipeResponse>> getAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.getAll(page, size);
    }

    @Operation(summary = "List all recipe reports")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @GetMapping("/report")
    public ResponseEntity<Page<RecipeReportResponse>> getAllReports(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.getAllReports(page, size);
    }

    @Operation(summary = "Review a recipe report")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @PatchMapping("/report/{id}")
    public ResponseEntity<String> reviewReport(
            Authentication authentication,
            @PathVariable("id") String id,
            @Valid @RequestBody RecipeReportReviewRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.reviewReport(UUID.fromString(id), userId, request);
    }
}