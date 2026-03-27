package fi.nutrifier.controllers;

import fi.nutrifier.dto.FoodReportCreateRequest;
import fi.nutrifier.dto.RecipeReportCreateRequest;
import fi.nutrifier.dto.RecipeRequest;
import fi.nutrifier.dto.RecipeResponse;
import fi.nutrifier.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Tag(name = "Recipes")
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    protected final RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    @Operation(summary = "Create a recipe")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping
    public ResponseEntity<RecipeResponse> create(
            Authentication authentication,
            @Valid @RequestBody RecipeRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.create(request, userId);
    }

    @Operation(summary = "Get recipe by id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getById(@PathVariable("id") String id) {
        return service.getById(UUID.fromString(id));
    }

    @Operation(summary = "Mark recipe as favourite")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping("/{id}/favourite")
    public ResponseEntity<String> markAsFavourite(Authentication authentication, @PathVariable("id") String id) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.markAsFavourite(UUID.fromString(id), userId);
    }

    @Operation(summary = "Remove recipe from favourites")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @DeleteMapping("/{id}/favourite")
    public ResponseEntity<String> removeFavourite(Authentication authentication, @PathVariable("id") String id) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.removeFavourite(UUID.fromString(id), userId);
    }

    @Operation(summary = "Get all favourite recipes")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/favourites")
    public ResponseEntity<List<RecipeResponse>> getAllFavourites(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.getAllFavourites(userId);
    }

    @Operation(summary = "Report a recipe")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping("/{id}/report")
    public ResponseEntity<String> report(
            Authentication authentication,
            @PathVariable("id") String id,
            @Valid @RequestBody RecipeReportCreateRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.report(UUID.fromString(id), userId, request);
    }
}