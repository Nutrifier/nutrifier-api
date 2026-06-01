package fi.nutrifier.controllers;

import fi.nutrifier.dto.FoodEntryRequest;
import fi.nutrifier.dto.FoodEntryResponse;
import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.enums.MealType;
import fi.nutrifier.services.FoodEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "Food Entries")
@RestController
@RequestMapping("/api/v1/food-entries")
public class FoodEntryController {

    protected final FoodEntryService service;

    public FoodEntryController(FoodEntryService service) {
        this.service = service;
    }

    @Operation(summary = "Create a log")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping
    public ResponseEntity<FoodEntryResponse> create(
            Authentication authentication,
            @Valid @RequestBody FoodEntryRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.create(userId, request);
    }

    @Operation(summary = "Update log")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping("/{id}")
    public ResponseEntity<FoodEntryResponse> update(
            Authentication authentication,
            @PathVariable("id") String id,
            @Valid @RequestBody FoodEntry item
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.update(userId, UUID.fromString(id), item);
    }

    @Operation(summary = "Delete log")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(Authentication authentication, @PathVariable("id") String id) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.delete(userId, UUID.fromString(id));
    }

    @Operation(summary = "Get logs by date and user id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping
    public ResponseEntity<List<FoodEntryResponse>> getLogsByDateAndUser(
            Authentication authentication,
            @RequestParam String date,
            @RequestParam(required = false) String mealType
    ) {
        MealType parsedMealType = mealType != null ? MealType.valueOf(mealType) : null;
        UUID userId = UUID.fromString(authentication.getName());
        LocalDate parsedDate = LocalDate.parse(date);
        return service.getLogsByDateAndMealTypeAndUserId(parsedDate, parsedMealType, userId);
    }

    @Operation(summary = "Recalculate nutrient snapshots with new nutrient values")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping("/recalculate")
    public ResponseEntity<FoodEntryResponse> recalculateSnapshots(
            Authentication authentication,
            @RequestParam String id
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return service.recalculateSnapshots(userId, UUID.fromString(id));
    }
}