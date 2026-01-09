package fi.nutrifier.controllers.admin;

import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.services.FoodEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Admin Logs Controller")
@RestController
@RequestMapping("/api/admin/logs")
public class AdminFoodEntryController {

    private final FoodEntryService service;

    public AdminFoodEntryController(FoodEntryService service) {
        this.service = service;
    }

    @Operation(summary = "Get all logs")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @GetMapping
    public ResponseEntity<Page<FoodEntry>> getAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.getAll(page, size);
    }

    @Operation(summary = "Get log by id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @GetMapping("/{id}")
    public ResponseEntity<FoodEntry> getById(@PathVariable("id") String id) {
        return service.getById(id);
    }

    @Operation(summary = "Get logs by user id")
    @SecurityRequirement(name = "bearerAuth", scopes = { "admin" })
    @GetMapping("/by-user/{id}")
    public ResponseEntity<List<FoodEntry>> getLogsByUserId(@PathVariable("id") String id) {
        return service.getLogsByUserId(id);
    }
}