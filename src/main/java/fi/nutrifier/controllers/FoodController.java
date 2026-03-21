package fi.nutrifier.controllers;

import fi.nutrifier.dto.FoodReportCreateRequest;
import fi.nutrifier.dto.FoodReportReviewRequest;
import fi.nutrifier.dto.FoodResponse;
import fi.nutrifier.services.FoodService;
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

import fi.nutrifier.dto.FoodRequest;

@Tag(name = "Foods")
@RestController
@RequestMapping("/api/foods")
public class FoodController {

    protected final FoodService service;

    public FoodController(FoodService service) {
        this.service = service;
    }

    @Operation(summary = "Create a food")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping
    public ResponseEntity<FoodResponse> create(
            Authentication authentication,
            @Valid @RequestBody FoodRequest foodRequest
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.create(foodRequest, userId);
    }

    @Operation(summary = "Get food by id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/{id}")
    public ResponseEntity<FoodResponse> getById(@PathVariable("id") String id) {
        return service.getById(UUID.fromString(id));
    }

    @Operation(summary = "Search for foods by name")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/query")
    public ResponseEntity<Page<FoodResponse>> getFoodsByQuery(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam String query
    ) {
        return service.getFoodsByQuery(page, size, query);
    }

    @Operation(summary = "Search for foods by barcode")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/barcode")
    public ResponseEntity<List<FoodResponse>> getFoodsByBarcode(@RequestParam String query) {
        return service.getFoodsByBarcode(query);
    }

    @Operation(summary = "Get all foods")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<Page<FoodResponse>> getAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.getAll(page, size);
    }

    @Operation(summary = "Get recently used foods")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/recent")
    public ResponseEntity<List<FoodResponse>> getRecentFoods(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.getRecentFoods(userId);
    }

    @Operation(summary = "Mark food as favourite")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping("/{id}/favourite")
    public ResponseEntity<String> markAsFavourite(Authentication authentication, @PathVariable("id") String id) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.markAsFavourite(UUID.fromString(id), userId);
    }

    @Operation(summary = "Remove food from favourites")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @DeleteMapping("/{id}/favourite")
    public ResponseEntity<String> removeFavourite(Authentication authentication, @PathVariable("id") String id) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.removeFavourite(UUID.fromString(id), userId);
    }

    @Operation(summary = "Get all favourite foods")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @DeleteMapping("/favourites")
    public ResponseEntity<List<FoodResponse>> getAllFavourites(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.getAllFavourites(userId);
    }

    @Operation(summary = "Report a food")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping("/{id}/report")
    public ResponseEntity<String> report(
            Authentication authentication,
            @PathVariable("id") String id,
            @Valid @RequestBody FoodReportCreateRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.report(UUID.fromString(id), userId, request);
    }
}