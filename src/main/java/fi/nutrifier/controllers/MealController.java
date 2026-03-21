package fi.nutrifier.controllers;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.services.FoodService;
import fi.nutrifier.services.MealService;
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

@Tag(name = "Meals")
@RestController
@RequestMapping("/api/meals")
public class MealController {

    protected final MealService service;

    public MealController(MealService service) {
        this.service = service;
    }

    @Operation(summary = "Create a meal")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping
    public ResponseEntity<MealResponse> create(
            Authentication authentication,
            @Valid @RequestBody MealRequest request
            ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.create(request, userId);
    }

    // TODO: Let only owner update meal, otherwise create a private copy
    @Operation(summary = "Update meal")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping("/{id}")
    public ResponseEntity<MealResponse> update(
            Authentication authentication,
            @PathVariable("id") String id,
            @Valid @RequestBody MealRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.update(userId, UUID.fromString(id), request);
    }

    @Operation(summary = "Get meal by id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/{id}")
    public ResponseEntity<MealResponse> getById(@PathVariable("id") String id) {
        return service.getById(UUID.fromString(id));
    }

    @Operation(summary = "Mark meal as favourite")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping("/{id}/favourite")
    public ResponseEntity<String> markAsFavourite(Authentication authentication, @PathVariable("id") String id) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.markAsFavourite(UUID.fromString(id), userId);
    }

    @Operation(summary = "Remove meal from favourites")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @DeleteMapping("/{id}/favourite")
    public ResponseEntity<String> removeFavourite(Authentication authentication, @PathVariable("id") String id) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.removeFavourite(UUID.fromString(id), userId);
    }

    @Operation(summary = "Get all favourite meals")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @DeleteMapping("/favourites")
    public ResponseEntity<List<MealResponse>> getAllFavourites(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.getAllFavourites(userId);
    }
}