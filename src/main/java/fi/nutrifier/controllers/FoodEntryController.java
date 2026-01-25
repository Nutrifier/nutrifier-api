package fi.nutrifier.controllers;

import fi.nutrifier.entities.FoodEntry;
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

@Tag(name = "Food Entries")
@RestController
@RequestMapping("/api/food-entries")
public class FoodEntryController {

    protected final FoodEntryService service;

    public FoodEntryController(FoodEntryService service) {
        this.service = service;
    }

    @Operation(summary = "Create a log")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping
    public ResponseEntity<FoodEntry> create(
            Authentication authentication,
            @Valid @RequestBody FoodEntry entity
    ) {
        String userId = authentication.getName();
        return service.create(userId, entity);
    }

    @Operation(summary = "Update log")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping("/{id}")
    public ResponseEntity<FoodEntry> update(
            Authentication authentication,
            @PathVariable("id") String id,
            @Valid @RequestBody FoodEntry item
    ) {
        String userId = authentication.getName();
        return service.update(userId, id, item);
    }

    @Operation(summary = "Delete log")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @DeleteMapping("/{id}")
    public ResponseEntity<FoodEntry> delete(Authentication authentication, @PathVariable("id") String id) {
        String userId = authentication.getName();
        return service.delete(userId, id);
    }

    @Operation(summary = "Get logs by date and user id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/by-date")
    public ResponseEntity<List<FoodEntry>> getLogsByDateAndUser(Authentication authentication, @RequestParam String date) {
        String userId = authentication.getName();
        LocalDate parsedDate = LocalDate.parse(date);
        return service.getLogsByDateAndUser(parsedDate, userId);
    }
}