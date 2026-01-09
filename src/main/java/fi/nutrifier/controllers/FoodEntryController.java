package fi.nutrifier.controllers;

import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.services.FoodEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Logs Controller")
@RestController
@RequestMapping("/api/logs")
public class FoodEntryController {

    protected final FoodEntryService service;

    public FoodEntryController(FoodEntryService service) {
        this.service = service;
    }

    @Operation(summary = "Create a log")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PostMapping
    public ResponseEntity<FoodEntry> create(@Valid @RequestBody FoodEntry entity) {
        return service.create(entity);
    }

    @Operation(summary = "Update log")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @PatchMapping("/{id}")
    public ResponseEntity<FoodEntry> update(@PathVariable("id") String id, @Valid @RequestBody FoodEntry item) {
        return service.update(id, item);
    }

    @Operation(summary = "Delete log")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @DeleteMapping("/{id}")
    public ResponseEntity<FoodEntry> delete(@PathVariable("id") String id) {
        return service.delete(id);
    }

    @Operation(summary = "Get logs by date and user id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "user" })
    @GetMapping("/by-date")
    public ResponseEntity<List<FoodEntry>> getLogsByDateAndUser(@RequestParam String date, @RequestParam String userId) {
        LocalDate parsedDate = LocalDate.parse(date);
        return service.getLogsByDateAndUser(parsedDate, userId);
    }
}